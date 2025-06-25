package com.elektro24team.auravindex.view


import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.annotation.RequiresPermission
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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.DrawerMenu
import androidx.navigation.NavController
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.ui.components.HomePageSection
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.utils.functions.hamburguerMenuNavigator
import com.elektro24team.auravindex.AuraVindexApp
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.model.RecentBook
import com.elektro24team.auravindex.model.local.NotificationEntity
import com.elektro24team.auravindex.ui.components.ConnectionAlert
import com.elektro24team.auravindex.ui.components.MustBeLoggedInDialog
import com.elektro24team.auravindex.ui.components.TopBar
import com.elektro24team.auravindex.utils.objects.NotificationHandler
import com.elektro24team.auravindex.utils.enums.AppAction
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektro24team.auravindex.utils.functions.shouldRequestNotificationPermission
import com.elektro24team.auravindex.utils.functions.isLoggedIn
import com.elektro24team.auravindex.utils.objects.AuthPrefsHelper
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.NotificationViewModel
import com.elektro24team.auravindex.viewmodels.RecentBookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
@Composable
fun MainScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    userViewModel: UserViewModel,
    notificationViewModel: NotificationViewModel,
    recentBookViewModel: RecentBookViewModel,
    localSettingViewModel: LocalSettingViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    rememberCoroutineScope()
    val context = LocalContext.current
    val activity = context as Activity
    val lifecycleOwner = LocalLifecycleOwner.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val books by bookViewModel.books.collectAsState()
    val latestReleases by bookViewModel.latestReleases.collectAsState()
    val localSettings by localSettingViewModel.settings.collectAsState()
    val recentBooks by recentBookViewModel.recentBook.observeAsState()
    var showMustBeLoggedInDialog by remember { mutableStateOf(false) }
    var actionMustBeLoggedInDialog by remember { mutableStateOf(AppAction.SUBSCRIBE_TO_PLAN) }
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            //Log.d("PERMISSIONTEST", "Notification permission granted")
        } else {
            val permanentlyDenied = !ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            if (permanentlyDenied) {
                //Log.d("PERMISSIONTEST", "Permission permanently denied. Guide user to settings.")
               // Toast.makeText(context, "Please enable notifications in app settings.", Toast.LENGTH_SHORT).show()
            } else {
                //Log.d("PERMISSIONTEST", "Notification permission denied (can ask again)")
            }
        }
    }
    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && shouldRequestNotificationPermission(context) && !AuthPrefsHelper.hasRequestedPermission(context)) {
                notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                AuthPrefsHelper.setPermissionRequested(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
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
                    drawerState = drawerState
                )
            },
            bottomBar = {
                BottomNavBar(
                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: "main",
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
                            .verticalScroll(rememberScrollState())
                            .padding(innerPadding),
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
                            latestReleases,
                            seeMoreAction = { navController.navigate(Routes.SEARCH) },
                            navController
                        )
                    }
                    /*Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {

                    }*/
                }
            }
        )
    }
}