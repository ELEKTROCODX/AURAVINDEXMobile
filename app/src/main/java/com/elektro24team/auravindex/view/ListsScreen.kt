package com.elektro24team.auravindex.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import com.elektro24team.auravindex.AuraVindexApp
import com.elektro24team.auravindex.ui.NotLoggedInAlert
import com.elektro24team.auravindex.ui.components.BookListCard
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.ConnectionAlert
import com.elektro24team.auravindex.ui.components.DrawerMenu
import com.elektro24team.auravindex.ui.components.ListForm
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.ui.components.TopBar
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveInsufficientPermissions
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveSuccess
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveTokenExpiration
import com.elektro24team.auravindex.utils.functions.hamburguerMenuNavigator
import com.elektro24team.auravindex.utils.functions.isLoggedIn
import com.elektro24team.auravindex.viewmodels.NotificationViewModel
import com.elektro24team.auravindex.viewmodels.BookListViewModel
import com.elektro24team.auravindex.viewmodels.LoanViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListsScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    notificationViewModel: NotificationViewModel,
    localSettingViewModel: LocalSettingViewModel,
    bookListViewModel: BookListViewModel,
    loanViewModel: LoanViewModel,
) {
    val localSettings = localSettingViewModel.settings.collectAsState()
    val userLists by bookListViewModel.bookLists.collectAsState()
    var showForm by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    rememberCoroutineScope()
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    LaunchedEffect(userLists) {
        if(isLoggedIn(localSettings.value)) {
            bookListViewModel.loadUserLists(localSettings.value[SettingKey.TOKEN.keySetting].toString(), localSettings.value[SettingKey.ID.keySetting].toString())
        }
    }
    ObserveTokenExpiration(bookListViewModel, navController, localSettingViewModel)
    ObserveInsufficientPermissions(bookListViewModel, navController)
    ObserveError(bookListViewModel)
    ObserveSuccess(bookListViewModel)
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
                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: "lists",
                    onItemClick = { route -> navController.navigate(route) }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showForm = true
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Create")
                }
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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        val app = LocalContext.current.applicationContext as AuraVindexApp
                        val isConnected by app.networkLiveData.observeAsState(true)
                        ConnectionAlert(isConnected)
                        NotLoggedInAlert(localSettings.value)
                        if(showForm){
                            ListForm(onDismiss = {showForm = false}, bookListViewModel = bookListViewModel, user = localSettings.value[SettingKey.ID.keySetting].toString(), token = localSettings.value[SettingKey.TOKEN.keySetting].toString(), context = context)
                        }
                        Text(
                            text = "My lists",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = 8.dp).align(Alignment.CenterHorizontally)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            // Active loans
                            // Pending loans

                            // Favorites
                            userLists?.forEach{ list ->
                                if((list.title == "Favorites") && (list.owner._id == localSettings.value[SettingKey.ID.keySetting].toString())) {
                                    BookListCard(list,navController, bookListViewModel, localSettings.value[SettingKey.TOKEN.keySetting].toString(), localSettings.value[SettingKey.ID.keySetting].toString())
                                }
                            }
                            // Custom
                            userLists?.forEach{ list ->
                                if((list.title != "Favorites") && (list.owner._id == localSettings.value[SettingKey.ID.keySetting].toString())) {
                                    BookListCard(list,navController, bookListViewModel, localSettings.value[SettingKey.TOKEN.keySetting].toString(), localSettings.value[SettingKey.ID.keySetting].toString())
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
