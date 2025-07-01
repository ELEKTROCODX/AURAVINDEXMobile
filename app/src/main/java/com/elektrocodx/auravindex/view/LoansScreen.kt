package com.elektrocodx.auravindex.view

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektrocodx.auravindex.AuraVindexApp
import com.elektrocodx.auravindex.ui.components.BottomNavBar
import com.elektrocodx.auravindex.ui.components.DrawerMenu
import com.elektrocodx.auravindex.ui.components.TopBar
import com.elektrocodx.auravindex.ui.components.alerts.ConnectionAlert
import com.elektrocodx.auravindex.ui.components.alerts.NotLoggedInAlert
import com.elektrocodx.auravindex.ui.components.cards.LoanCard
import com.elektrocodx.auravindex.ui.components.dialogs.ShowExternalLinkDialog
import com.elektrocodx.auravindex.utils.enums.SettingKey
import com.elektrocodx.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektrocodx.auravindex.utils.functions.APIerrorHandlers.ObserveInsufficientPermissions
import com.elektrocodx.auravindex.utils.functions.APIerrorHandlers.ObserveTokenExpiration
import com.elektrocodx.auravindex.utils.functions.hamburguerMenuNavigator
import com.elektrocodx.auravindex.utils.functions.isLoggedIn
import com.elektrocodx.auravindex.viewmodels.LoanViewModel
import com.elektrocodx.auravindex.viewmodels.LocalSettingViewModel
import com.elektrocodx.auravindex.viewmodels.NotificationViewModel
import com.elektrocodx.auravindex.viewmodels.UserViewModel


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoansScreen(
    navController: NavController,
    loanViewModel: LoanViewModel,
    notificationViewModel: NotificationViewModel,
    localSettingViewModel: LocalSettingViewModel,
    userViewModel: UserViewModel,

) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    rememberCoroutineScope()
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val userLoans by loanViewModel.userLoans.collectAsState()
    val localSettings by localSettingViewModel.settings.collectAsState()
    var currentPage by remember { mutableIntStateOf(0) }
    val itemsPerPage by remember { mutableIntStateOf(6) }
    val paginatedLoans = userLoans?.drop(currentPage * itemsPerPage)?.take(itemsPerPage)
    val totalPages = (userLoans?.size?.plus(itemsPerPage)?.minus(1))?.div(itemsPerPage)
    LaunchedEffect(Unit) {
        if(isLoggedIn(localSettings)) {
            loanViewModel.loadUserLoans(
                localSettings.getOrDefault(SettingKey.TOKEN.keySetting, ""),
                localSettings.getOrDefault(SettingKey.ID.keySetting, "")
            )
        }
    }
    ObserveTokenExpiration(loanViewModel, navController, localSettingViewModel)
    ObserveInsufficientPermissions(loanViewModel, navController)
    ObserveError(loanViewModel)
    ModalNavigationDrawer(
        drawerContent = {
            DrawerMenu(
                navController = navController,
                currentRoute = navController.currentBackStackEntry?.destination?.route,
                userViewModel = userViewModel,
                notificationViewModel = notificationViewModel,
                localSettingViewModel = localSettingViewModel,
                onItemSelected = { route ->
                    hamburguerMenuNavigator(
                        route,
                        navController,
                        showTermsDialog,
                        showPrivacyDialog,
                        showTeamDialog
                    )
                }
            )
        },
        drawerState = drawerState
    ) {
        ShowExternalLinkDialog(showTermsDialog, context, "https://auravindex.me/tos/")
        ShowExternalLinkDialog(showPrivacyDialog, context, "https://auravindex.me/privacy/")
        ShowExternalLinkDialog(showTeamDialog, context, "https://auravindex.me/about/")
        Scaffold(
            topBar = {
                TopBar(
                    navController = navController,
                    drawerState = drawerState,
                    localSettingViewModel = localSettingViewModel,
                    notificationViewModel = notificationViewModel
                )
            },
            bottomBar = {
                BottomNavBar(
                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: "search",
                    onItemClick = { route -> navController.navigate(route) }
                )
            },
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFFEDE7F6), Color(0xFFD1C4E9))
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val app = LocalContext.current.applicationContext as AuraVindexApp
                        val isConnected by app.networkLiveData.observeAsState(true)
                        ConnectionAlert(isConnected)
                        NotLoggedInAlert(localSettings)
                        Text(
                            text = "My loans",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(vertical = 16.dp).align(Alignment.CenterHorizontally)
                        )
                        if (paginatedLoans != null) {
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(paginatedLoans?.size ?: 0) { index ->
                                    LoanCard(loan = paginatedLoans?.get(index), navController = navController)
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth().horizontalScroll(
                                    rememberScrollState()
                                ),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                if(totalPages != null) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Button(
                                            onClick = { if (currentPage > 0) currentPage-- },
                                            enabled = currentPage > 0
                                        ) {
                                            Text("Prev")
                                        }

                                        Text(
                                            text = "Page ${currentPage + 1} of $totalPages",
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )

                                        Button(
                                            onClick = { if (currentPage < totalPages - 1) currentPage++ },
                                            enabled = currentPage < totalPages - 1
                                        ) {
                                            Text("Next")
                                        }
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "You don't have any loans.",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}
