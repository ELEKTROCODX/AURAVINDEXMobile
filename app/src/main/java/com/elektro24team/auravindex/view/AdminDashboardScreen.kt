package com.elektro24team.auravindex.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.elektro24team.auravindex.ui.components.AdminActivePlanTable
import com.elektro24team.auravindex.ui.components.AdminAuditLogTable
import com.elektro24team.auravindex.ui.components.AdminBookCard
import com.elektro24team.auravindex.ui.components.AdminBookTable
import com.elektro24team.auravindex.ui.components.AdminLoanTable
import com.elektro24team.auravindex.ui.components.AdminPlanCard
import com.elektro24team.auravindex.ui.components.AdminPlanTable
import com.elektro24team.auravindex.ui.components.AdminUserCard
import com.elektro24team.auravindex.ui.components.AdminUserTable
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.utils.functions.hamburguerMenuNavigator
import com.elektro24team.auravindex.ui.components.TopBar
import com.elektro24team.auravindex.utils.enums.AdminDashboardObject
import com.elektro24team.auravindex.utils.enums.AppAction
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveInsufficientPermissions
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveTokenExpiration
import com.elektro24team.auravindex.viewmodels.ActivePlanViewModel
import com.elektro24team.auravindex.viewmodels.AuditLogViewModel
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.elektro24team.auravindex.viewmodels.LoanViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.PlanViewModel

@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    userViewModel: UserViewModel,
    loanViewModel: LoanViewModel,
    planViewModel: PlanViewModel,
    activePlanViewModel: ActivePlanViewModel,
    auditLogViewModel: AuditLogViewModel,
    localSettingViewModel: LocalSettingViewModel,
    objectName: String?,
    objectId: String?
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    rememberCoroutineScope()
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val localSettings by localSettingViewModel.settings.collectAsState()
    val isLoggedIn = localSettings.getOrDefault(SettingKey.TOKEN.keySetting, "").isNotEmpty()
    var showMustBeLoggedInDialog by remember { mutableStateOf(false) }
    var actionMustBeLoggedInDialog by remember { mutableStateOf(AppAction.ACCESS_ADMIN_DASHBOARD) }
    MaterialTheme.colorScheme
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
                userViewModel = userViewModel, // <- este es el parámetro faltante
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
                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: "admin_dashboard",
                    onItemClick = { route -> navController.navigate(route) }
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
                            * Check pending loans
                            * */
                        } else if(objectId == null || objectId == "") {
                            when(objectName) {
                                AdminDashboardObject.BOOK.name.lowercase() -> {
                                    ObserveTokenExpiration(bookViewModel, navController, localSettingViewModel)
                                    ObserveInsufficientPermissions(bookViewModel, navController)
                                    val books by bookViewModel.books.observeAsState()
                                    LaunchedEffect(Unit) {
                                        bookViewModel.loadBooks(showDuplicates = true, showLents = true)
                                    }
                                    AdminBookTable(navController, books ?: emptyList())
                                }
                                AdminDashboardObject.PLAN.name.lowercase() -> {
                                    ObserveTokenExpiration(planViewModel, navController, localSettingViewModel)
                                    ObserveInsufficientPermissions(planViewModel, navController)
                                    val plans by planViewModel.plans.observeAsState()
                                    LaunchedEffect(Unit) {
                                        planViewModel.loadPlans()
                                    }
                                    AdminPlanTable(navController, plans ?: emptyList())
                                }
                                AdminDashboardObject.USER.name.lowercase() -> {
                                    ObserveTokenExpiration(userViewModel, navController, localSettingViewModel)
                                    ObserveInsufficientPermissions(userViewModel, navController)
                                    ObserveError(userViewModel)
                                    val users by userViewModel.users.observeAsState()
                                    LaunchedEffect(Unit) {
                                        userViewModel.getUsers(localSettings.getOrDefault(SettingKey.TOKEN.keySetting, ""))
                                    }
                                    AdminUserTable(navController, users ?: emptyList())
                                }
                                AdminDashboardObject.LOAN.name.lowercase() -> {
                                    ObserveTokenExpiration(loanViewModel, navController, localSettingViewModel)
                                    ObserveInsufficientPermissions(loanViewModel, navController)
                                    ObserveError(loanViewModel)
                                    val loans by loanViewModel.loans.observeAsState()
                                    LaunchedEffect(Unit) {
                                        loanViewModel.loadLoans(localSettings.getOrDefault(SettingKey.TOKEN.keySetting, ""))
                                    }
                                    AdminLoanTable(navController, loans ?: emptyList())
                                }
                                AdminDashboardObject.ACTIVE_PLAN.name.lowercase() -> {
                                    ObserveTokenExpiration(activePlanViewModel, navController, localSettingViewModel)
                                    ObserveInsufficientPermissions(activePlanViewModel, navController)
                                    ObserveError(activePlanViewModel)
                                    val activePlans by activePlanViewModel.activePlans.observeAsState()
                                    LaunchedEffect(Unit) {
                                        activePlanViewModel.loadActivePlans(localSettings.getOrDefault(SettingKey.TOKEN.keySetting, ""))
                                    }
                                    AdminActivePlanTable(navController, activePlans ?: emptyList())
                                }
                                AdminDashboardObject.AUDIT_LOG.name.lowercase() -> {
                                    ObserveTokenExpiration(auditLogViewModel, navController, localSettingViewModel)
                                    ObserveInsufficientPermissions(auditLogViewModel, navController)
                                    ObserveError(auditLogViewModel)
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
                                    ObserveInsufficientPermissions(bookViewModel, navController)
                                    AdminBookCard(
                                        navController = navController,
                                        bookViewModel = bookViewModel,
                                        loanViewModel = loanViewModel,
                                        localSettingViewModel = localSettingViewModel,
                                        bookId = objectId,
                                    )
                                }
                                AdminDashboardObject.PLAN.name.lowercase() -> {
                                    ObserveTokenExpiration(planViewModel, navController, localSettingViewModel)
                                    ObserveInsufficientPermissions(planViewModel, navController)
                                    AdminPlanCard(
                                        navController = navController,
                                        planViewModel = planViewModel,
                                        planId = objectId,
                                    )
                                }
                                AdminDashboardObject.USER.name.lowercase() -> {
                                    ObserveTokenExpiration(userViewModel, navController, localSettingViewModel)
                                    ObserveInsufficientPermissions(userViewModel, navController)
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
