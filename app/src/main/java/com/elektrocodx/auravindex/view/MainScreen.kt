package com.elektrocodx.auravindex.view


import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.elektrocodx.auravindex.AuraVindexApp
import com.elektrocodx.auravindex.navigation.Routes
import com.elektrocodx.auravindex.ui.components.BottomNavBar
import com.elektrocodx.auravindex.ui.components.DrawerMenu
import com.elektrocodx.auravindex.ui.components.HomePageSection
import com.elektrocodx.auravindex.ui.components.TopBar
import com.elektrocodx.auravindex.ui.components.alerts.ConnectionAlert
import com.elektrocodx.auravindex.ui.components.dialogs.ShowExternalLinkDialog
import com.elektrocodx.auravindex.utils.enums.SettingKey
import com.elektrocodx.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektrocodx.auravindex.utils.functions.hamburguerMenuNavigator
import com.elektrocodx.auravindex.utils.functions.isLoggedIn
import com.elektrocodx.auravindex.utils.functions.shouldRequestNotificationPermission
import com.elektrocodx.auravindex.utils.objects.AuthPrefsHelper
import com.elektrocodx.auravindex.viewmodels.BookViewModel
import com.elektrocodx.auravindex.viewmodels.LocalSettingViewModel
import com.elektrocodx.auravindex.viewmodels.NotificationViewModel
import com.elektrocodx.auravindex.viewmodels.RecentBookViewModel
import com.elektrocodx.auravindex.viewmodels.UserViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
                    drawerState = drawerState,
                    localSettingViewModel = localSettingViewModel,
                    notificationViewModel = notificationViewModel
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()) ,
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
                }
            }
        )
    }
}