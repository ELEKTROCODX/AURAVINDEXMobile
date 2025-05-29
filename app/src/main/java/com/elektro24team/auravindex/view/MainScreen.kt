package com.elektro24team.auravindex.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.DrawerMenu
import androidx.navigation.NavController
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.ui.components.HomePageSection
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.utils.functions.hamburguerMenuNavigator
import com.elektro24team.auravindex.AuraVindexApp
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.ui.components.ConnectionAlert
import com.elektro24team.auravindex.ui.components.MustBeLoggedInDialog
import com.elektro24team.auravindex.ui.components.TopBar
import com.elektro24team.auravindex.utils.enums.AppAction
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektro24team.auravindex.utils.functions.isLoggedIn
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.RecentBookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    userViewModel: UserViewModel,
    recentBookViewModel: RecentBookViewModel,
    localSettingViewModel: LocalSettingViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    rememberCoroutineScope()
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val books by bookViewModel.books.observeAsState(emptyList())
    val latestReleases by bookViewModel.latestReleases.observeAsState(emptyList()) // Cambiado de 'val latestReleases = ...' a 'val latestReleases by ...'
    val localSettings by localSettingViewModel.settings.collectAsState()
    val recentBooks by recentBookViewModel.recentBook.observeAsState()
    var showMustBeLoggedInDialog by remember { mutableStateOf(false) }
    var actionMustBeLoggedInDialog by remember { mutableStateOf(AppAction.SUBSCRIBE_TO_PLAN) }

    LaunchedEffect(Unit) {
        bookViewModel.loadBooks(showDuplicates = false, showLents = true)
        localSettingViewModel.loadUserSettings()
        bookViewModel.fetchLatestReleases()
        if(isLoggedIn(localSettings)) {
            recentBookViewModel.loadRecentBooks(localSettings[SettingKey.TOKEN.keySetting] ?: "", localSettings[SettingKey.ID.keySetting] ?: "")
        }
    }
    ObserveError(bookViewModel)
    ObserveError(userViewModel)
    ObserveError(recentBookViewModel)
    ModalNavigationDrawer(
        drawerContent = {
            DrawerMenu(
                navController = navController,
                currentRoute = navController.currentBackStackEntry?.destination?.route,
                userViewModel = userViewModel,
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

                        if (showMustBeLoggedInDialog) {
                            MustBeLoggedInDialog(
                                navController = navController,
                                action = actionMustBeLoggedInDialog,
                                onDismiss = { showMustBeLoggedInDialog = false }
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if(isLoggedIn(localSettings) && recentBooks?.books?.size!=0 ) {
                                HomePageSection(
                                    "Recent searches",
                                    recentBooks?.books,
                                    seeMoreAction = { navController.navigate(Routes.SEARCH) },
                                    navController
                                )
                            }
                            HomePageSection(
                                "Recommendations",
                                books?.take(10),
                                seeMoreAction = { navController.navigate(Routes.SEARCH) },
                                navController
                            )
                            HomePageSection(
                                "New releases",
                                latestReleases, // Eliminado el 'as List<Book>?' ya que 'latestReleases' ya es una List<Book>
                                seeMoreAction = { navController.navigate(Routes.SEARCH) },
                                navController
                            )
                        }
                    }
                }
            }
        )
    }
}