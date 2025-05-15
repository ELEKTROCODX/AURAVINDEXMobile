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
import com.elektro24team.auravindex.ui.components.AdminBookTable
import com.elektro24team.auravindex.ui.components.HomePageSection
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.utils.hamburguerMenuNavigator
import com.elektro24team.auravindex.ui.components.TopBar
import com.elektro24team.auravindex.utils.enums.AppAction
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.elektro24team.auravindex.viewmodels.BookViewModelOld
import com.elektro24team.auravindex.viewmodels.UserViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashBoardScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    userViewModel: UserViewModel,
    localSettingsViewModel: LocalSettingViewModel,
    objectName: String?,
    objectId: String?
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val localSettings by localSettingsViewModel.settings.collectAsState()
    val isLoggedIn = localSettings.getOrDefault(SettingKey.TOKEN.keySetting, "").isNotEmpty()
    var showMustBeLoggedInDialog by remember { mutableStateOf(false) }
    var actionMustBeLoggedInDialog by remember { mutableStateOf(AppAction.ACCESS_ADMIN_DASHBOARD) }

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
                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: "admin_dashboard",
                    onItemClick = { route -> navController.navigate(route) }
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
                        if(isLoggedIn) {
                            actionMustBeLoggedInDialog = AppAction.ACCESS_ADMIN_DASHBOARD
                            showMustBeLoggedInDialog = true
                        }

                        /*
                        * Case 1: Object name and ID are null (show welcome screen)
                        * Case 2: Object name is not null but ID is null (show object table)
                        * Case 3: Object name and ID are not null (show object details)
                        * */
                        if(objectName == null || objectName == "") {
                            Text(
                                text = "Admin Dashboard",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(vertical = 16.dp).align(Alignment.CenterHorizontally)
                            )
                            Button(
                                onClick = {navController.navigate("admin_dashboard/book")}
                            ) {
                                Text("Books")
                            }
                        } else if(objectId == null || objectId == "") {
                            AdminBookTable(
                                navController = navController,
                                books = bookViewModel.books.observeAsState().value ?: emptyList()
                            )
                        } else if((objectName != null || objectName != "") && (objectId != null || objectId != "")) {
                            Text("None null")
                        }
                    }
                }
            }
        )
    }
}
