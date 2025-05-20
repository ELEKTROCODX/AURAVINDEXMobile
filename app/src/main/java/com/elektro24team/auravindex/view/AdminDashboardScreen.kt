package com.elektro24team.auravindex.view

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.*
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
import com.elektro24team.auravindex.ui.components.AdminAuditLogTable
import com.elektro24team.auravindex.ui.components.AdminBookCard
import com.elektro24team.auravindex.ui.components.AdminBookTable
import com.elektro24team.auravindex.ui.components.AdminPlanCard
import com.elektro24team.auravindex.ui.components.AdminPlanTable
import com.elektro24team.auravindex.ui.components.AdminUserCard
import com.elektro24team.auravindex.ui.components.AdminUserTable
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.utils.hamburguerMenuNavigator
import com.elektro24team.auravindex.ui.components.TopBar
import com.elektro24team.auravindex.utils.enums.AdminDashboardObject
import com.elektro24team.auravindex.utils.enums.AppAction
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveInsufficentPermissions
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveTokenExpiration
import com.elektro24team.auravindex.viewmodels.AuditLogViewModel
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.PlanViewModel

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashBoardScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    userViewModel: UserViewModel,
    planViewModel: PlanViewModel,
    auditLogViewModel: AuditLogViewModel,
    localSettingViewModel: LocalSettingViewModel,
    objectName: String?,
    objectId: String?
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val localSettings by localSettingViewModel.settings.collectAsState()
    val isLoggedIn = localSettings.getOrDefault(SettingKey.TOKEN.keySetting, "").isNotEmpty()
    var showMustBeLoggedInDialog by remember { mutableStateOf(false) }
    var actionMustBeLoggedInDialog by remember { mutableStateOf(AppAction.ACCESS_ADMIN_DASHBOARD) }
    val colors = MaterialTheme.colorScheme
    LaunchedEffect(Unit) {
        localSettingViewModel.loadSettings(
            SettingKey.TOKEN.keySetting,
            SettingKey.ID.keySetting,
            SettingKey.EMAIL.keySetting,
        )
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
                            /*
                            * Admin actions:
                            * Send notifications
                            * Check statistics
                            * */
                        } else if(objectId == null || objectId == "") {
                            when(objectName) {
                                AdminDashboardObject.BOOK.name.lowercase() -> {
                                    ObserveTokenExpiration(bookViewModel, navController, localSettingViewModel)
                                    ObserveInsufficentPermissions(bookViewModel, navController)
                                    val books by bookViewModel.books.observeAsState()
                                    LaunchedEffect(Unit) {
                                        bookViewModel.loadBooks(showDuplicates = true, showLents = true)
                                    }
                                    AdminBookTable(navController, books ?: emptyList())
                                }
                                AdminDashboardObject.PLAN.name.lowercase() -> {
                                    ObserveTokenExpiration(planViewModel, navController, localSettingViewModel)
                                    ObserveInsufficentPermissions(planViewModel, navController)
                                    val plans by planViewModel.plans.observeAsState()
                                    LaunchedEffect(Unit) {
                                        planViewModel.loadPlans()
                                    }
                                    AdminPlanTable(navController, plans ?: emptyList())
                                }
                                AdminDashboardObject.USER.name.lowercase() -> {
                                    ObserveTokenExpiration(userViewModel, navController, localSettingViewModel)
                                    ObserveInsufficentPermissions(userViewModel, navController)
                                    ObserveError(userViewModel)
                                    val users by userViewModel.users.observeAsState()
                                    LaunchedEffect(Unit) {
                                        userViewModel.getUsers(localSettings.getOrDefault(SettingKey.TOKEN.keySetting, ""))
                                    }
                                    AdminUserTable(navController, users ?: emptyList())
                                }
                                AdminDashboardObject.AUDIT_LOG.name.lowercase() -> {
                                    ObserveTokenExpiration(auditLogViewModel, navController, localSettingViewModel)
                                    ObserveInsufficentPermissions(auditLogViewModel, navController)
                                    val auditLogs by auditLogViewModel.auditLogs.observeAsState()
                                    LaunchedEffect(Unit) {
                                        auditLogViewModel.getAuditLogs(localSettings.getOrDefault(SettingKey.TOKEN.keySetting, ""))
                                    }
                                    AdminAuditLogTable(navController, auditLogs ?: emptyList())
                                }
                                else -> {
                                    Text("Unknown object")
                                }
                            }
                        } else if((objectName != null || objectName != "") && (objectId != null || objectId != "")) {
                            when(objectName) {
                                AdminDashboardObject.BOOK.name.lowercase() -> {
                                    ObserveTokenExpiration(bookViewModel, navController, localSettingViewModel)
                                    ObserveInsufficentPermissions(bookViewModel, navController)
                                    AdminBookCard(
                                        navController = navController,
                                        bookViewModel = bookViewModel,
                                        bookId = objectId,
                                    )
                                }
                                AdminDashboardObject.PLAN.name.lowercase() -> {
                                    ObserveTokenExpiration(planViewModel, navController, localSettingViewModel)
                                    ObserveInsufficentPermissions(planViewModel, navController)
                                    AdminPlanCard(
                                        navController = navController,
                                        planViewModel = planViewModel,
                                        planId = objectId,
                                    )
                                }
                                AdminDashboardObject.USER.name.lowercase() -> {
                                    ObserveTokenExpiration(userViewModel, navController, localSettingViewModel)
                                    ObserveInsufficentPermissions(userViewModel, navController)
                                    AdminUserCard(
                                        navController = navController,
                                        userViewModel = userViewModel,
                                        localSettingViewModel = localSettingViewModel,
                                        userId = objectId,
                                    )
                                }
                                else -> {
                                    Text("Unknown object")
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
