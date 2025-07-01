package com.elektrocodx.auravindex.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.elektrocodx.auravindex.AuraVindexApp
import com.elektrocodx.auravindex.ui.components.BottomNavBar
import com.elektrocodx.auravindex.ui.components.DrawerMenu
import com.elektrocodx.auravindex.ui.components.ListForm
import com.elektrocodx.auravindex.ui.components.TopBar
import com.elektrocodx.auravindex.ui.components.alerts.ConnectionAlert
import com.elektrocodx.auravindex.ui.components.alerts.NotLoggedInAlert
import com.elektrocodx.auravindex.ui.components.cards.BookListCard
import com.elektrocodx.auravindex.ui.components.dialogs.ShowExternalLinkDialog
import com.elektrocodx.auravindex.utils.enums.AppAction
import com.elektrocodx.auravindex.utils.enums.SettingKey
import com.elektrocodx.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektrocodx.auravindex.utils.functions.APIerrorHandlers.ObserveInsufficientPermissions
import com.elektrocodx.auravindex.utils.functions.APIerrorHandlers.ObserveSuccess
import com.elektrocodx.auravindex.utils.functions.APIerrorHandlers.ObserveTokenExpiration
import com.elektrocodx.auravindex.utils.functions.hamburguerMenuNavigator
import com.elektrocodx.auravindex.utils.functions.isLoggedIn
import com.elektrocodx.auravindex.utils.functions.mustBeLoggedInToast
import com.elektrocodx.auravindex.viewmodels.BookListViewModel
import com.elektrocodx.auravindex.viewmodels.LocalSettingViewModel
import com.elektrocodx.auravindex.viewmodels.NotificationViewModel
import com.elektrocodx.auravindex.viewmodels.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListsScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    notificationViewModel: NotificationViewModel,
    localSettingViewModel: LocalSettingViewModel,
    bookListViewModel: BookListViewModel,
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
                TopBar(
                    navController = navController,
                    drawerState = drawerState,
                    localSettingViewModel = localSettingViewModel,
                    notificationViewModel = notificationViewModel
                )
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
                        if(isLoggedIn(localSettings.value)) {
                            showForm = true
                        } else {
                            mustBeLoggedInToast(context, AppAction.CREATE_LIST, navController)
                        }
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
                        if(showForm){
                            ListForm(
                                navController = navController,
                                onDismiss = { showForm = false },
                                bookListViewModel = bookListViewModel,
                                localSettingViewModel = localSettingViewModel,
                                token = localSettings.value[SettingKey.TOKEN.keySetting].toString(),
                                user = localSettings.value[SettingKey.ID.keySetting].toString(),
                                context = context
                            )
                        }
                        val app = LocalContext.current.applicationContext as AuraVindexApp
                        val isConnected by app.networkLiveData.observeAsState(true)
                        ConnectionAlert(isConnected)
                        NotLoggedInAlert(localSettings.value)
                        Text(
                            text = "My lists",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = 8.dp).align(Alignment.CenterHorizontally)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
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
