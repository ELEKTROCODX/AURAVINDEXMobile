package com.elektro24team.auravindex.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektro24team.auravindex.AuraVindexApp
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.ui.components.BookCollectionsSection
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.ConnectionAlert
import com.elektro24team.auravindex.ui.components.DrawerMenu
import com.elektro24team.auravindex.ui.components.LoanCard
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.ui.components.TopBar
import com.elektro24team.auravindex.ui.theme.BlackC
import com.elektro24team.auravindex.ui.theme.BrownC
import com.elektro24team.auravindex.ui.theme.OrangeC
import com.elektro24team.auravindex.ui.theme.PurpleC
import com.elektro24team.auravindex.ui.theme.WhiteC
import com.elektro24team.auravindex.utils.constants.URLs.IMG_url
import com.elektro24team.auravindex.utils.enums.AppAction
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveInsufficientPermissions
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveTokenExpiration
import com.elektro24team.auravindex.utils.functions.hamburguerMenuNavigator
import com.elektro24team.auravindex.utils.functions.isLoggedIn
import com.elektro24team.auravindex.utils.functions.mustBeLoggedInToast
import com.elektro24team.auravindex.viewmodels.BookCollectionViewModel
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.elektro24team.auravindex.viewmodels.LoanViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.NotificationViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage


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
    val settings by localSettingViewModel.settings.collectAsState()
    var currentPage by remember { mutableIntStateOf(1) }
    val itemsPerPage by remember { mutableIntStateOf(6) }
    val paginatedLoans = userLoans?.drop((currentPage - 1) * itemsPerPage)?.take(itemsPerPage)
    val totalPages = (userLoans?.size?.plus(itemsPerPage)?.minus(1))?.div(itemsPerPage)
    LaunchedEffect(Unit) {
        if(isLoggedIn(settings)) {
            loanViewModel.loadUserLoans(
                settings.getOrDefault(SettingKey.TOKEN.keySetting, ""),
                settings.getOrDefault(SettingKey.ID.keySetting, "")
            )
        }
    }
    ObserveTokenExpiration(loanViewModel, navController, localSettingViewModel)
    ObserveInsufficientPermissions(loanViewModel, navController)
    ObserveError(loanViewModel)
    if(!isLoggedIn(settings)) {
        mustBeLoggedInToast(context, AppAction.ACCESS_LOANS, navController)
    }
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
                TopBar(navController = navController, drawerState = drawerState)
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
                                    (1..totalPages).forEach { page ->
                                        Button(
                                            onClick = { currentPage = page },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (page == currentPage)
                                                    MaterialTheme.colorScheme.primary
                                                else
                                                    MaterialTheme.colorScheme.secondaryContainer,
                                                contentColor = if (page == currentPage)
                                                    MaterialTheme.colorScheme.onPrimary
                                                else
                                                    MaterialTheme.colorScheme.onSecondaryContainer
                                            ),
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        ) {
                                            Text(text = "$page")
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
