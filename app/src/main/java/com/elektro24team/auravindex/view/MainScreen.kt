package com.elektro24team.auravindex.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.DrawerMenu
import androidx.navigation.NavController
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.ui.components.HomePageSection
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.utils.hamburguerMenuNavigator
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elektro24team.auravindex.AuraVindexApp
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.ui.components.ConnectionAlert
import com.elektro24team.auravindex.ui.components.MustBeLoggedInDialog
import com.elektro24team.auravindex.ui.components.TopBar
import com.elektro24team.auravindex.utils.enums.AppAction
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.isLoggedIn
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.elektro24team.auravindex.viewmodels.BookViewModelOld
import com.elektro24team.auravindex.viewmodels.UserViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    userViewModel: UserViewModel,
    localSettingsViewModel: LocalSettingViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val books by bookViewModel.books.observeAsState(emptyList())
    val latestReleases by bookViewModel.latestReleases.observeAsState(emptyList())
    val user by userViewModel.user.observeAsState()
    val localSettings by localSettingsViewModel.settings.collectAsState()
    var showMustBeLoggedInDialog by remember { mutableStateOf(false) }
    var actionMustBeLoggedInDialog by remember { mutableStateOf(AppAction.SUBSCRIBE_TO_PLAN) }

    LaunchedEffect(Unit) {
        bookViewModel.loadBooks(showDuplicates = false, showLents = true)
        bookViewModel.fetchLatestReleases()
    }
    ModalNavigationDrawer(
        drawerContent = {
            DrawerMenu(
                navController = navController,
                currentRoute = navController.currentBackStackEntry?.destination?.route,
                onItemSelected = { route ->
                hamburguerMenuNavigator(
                    route,
                    navController,
                    showTermsDialog,
                    showPrivacyDialog,
                    showTeamDialog
                )
            })

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
                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: "main",
                    onItemClick = { route ->
                        if((route == Routes.PLANS || route == Routes.LISTS) && !isLoggedIn(localSettings)) {
                            showMustBeLoggedInDialog = true
                            if(route == Routes.PLANS) actionMustBeLoggedInDialog = AppAction.SUBSCRIBE_TO_PLAN
                            if(route == Routes.LISTS) actionMustBeLoggedInDialog = AppAction.CHECK_LISTS
                        } else {
                            navController.navigate(route)
                        }
                    }
                )
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        user?.let{
                            Text("Bienvenido: ${user?.name}")
                        }
                        val app = LocalContext.current.applicationContext as AuraVindexApp
                        val isConnected by app.networkLiveData.observeAsState(true)
                        ConnectionAlert(isConnected)

                        if (showMustBeLoggedInDialog) {
                            MustBeLoggedInDialog(
                                navController = navController,
                                action = actionMustBeLoggedInDialog,
                                onDismiss = { showMustBeLoggedInDialog = false }
                            )
                        }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Recent searches

                            // Recommendations
                            item {
                                HomePageSection(
                                    "Recommendations",
                                    books.take(10),
                                    seeMoreAction = { navController.navigate(Routes.SEARCH) },
                                    navController
                                )
                            }
                            // New releases
                            item {
                                HomePageSection(
                                    "New releases",
                                    latestReleases,
                                    seeMoreAction = { navController.navigate(Routes.SEARCH) },
                                    navController
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
