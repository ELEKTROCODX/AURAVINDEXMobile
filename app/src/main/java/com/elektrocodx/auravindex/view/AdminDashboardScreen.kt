package com.elektrocodx.auravindex.view

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektrocodx.auravindex.AuraVindexApp
import com.elektrocodx.auravindex.R
import com.elektrocodx.auravindex.navigation.Routes
import com.elektrocodx.auravindex.ui.components.BottomNavBar
import com.elektrocodx.auravindex.ui.components.DrawerMenu
import com.elektrocodx.auravindex.ui.components.TopBar
import com.elektrocodx.auravindex.ui.components.alerts.ConnectionAlert
import com.elektrocodx.auravindex.ui.components.cards.AdminLoanCard
import com.elektrocodx.auravindex.ui.components.cards.AdminPlanCard
import com.elektrocodx.auravindex.ui.components.cards.AdminUserCard
import com.elektrocodx.auravindex.ui.components.dialogs.ShowExternalLinkDialog
import com.elektrocodx.auravindex.ui.components.tables.AdminActivePlanTable
import com.elektrocodx.auravindex.ui.components.tables.AdminAuditLogTable
import com.elektrocodx.auravindex.ui.components.tables.AdminBookTable
import com.elektrocodx.auravindex.ui.components.tables.AdminLoanTable
import com.elektrocodx.auravindex.ui.components.tables.AdminNotificationTable
import com.elektrocodx.auravindex.ui.components.tables.AdminPlanTable
import com.elektrocodx.auravindex.ui.components.tables.AdminUserTable
import com.elektrocodx.auravindex.utils.enums.AdminDashboardObject
import com.elektrocodx.auravindex.utils.enums.SettingKey
import com.elektrocodx.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektrocodx.auravindex.utils.functions.APIerrorHandlers.ObserveInsufficientPermissions
import com.elektrocodx.auravindex.utils.functions.APIerrorHandlers.ObserveTokenExpiration
import com.elektrocodx.auravindex.utils.functions.hamburguerMenuNavigator
import com.elektrocodx.auravindex.viewmodels.ActivePlanViewModel
import com.elektrocodx.auravindex.viewmodels.AuditLogViewModel
import com.elektrocodx.auravindex.viewmodels.BookViewModel
import com.elektrocodx.auravindex.viewmodels.LoanViewModel
import com.elektrocodx.auravindex.viewmodels.LocalSettingViewModel
import com.elektrocodx.auravindex.viewmodels.NotificationViewModel
import com.elektrocodx.auravindex.viewmodels.PlanViewModel
import com.elektrocodx.auravindex.viewmodels.UserViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
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
    notificationViewModel: NotificationViewModel,
    localSettingViewModel: LocalSettingViewModel,
    objectName: String?,
    objectId: String?
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val localSettings by localSettingViewModel.settings.collectAsState()

    LaunchedEffect(Unit) {
        localSettingViewModel.loadSettings(
            SettingKey.TOKEN.keySetting,
            SettingKey.ID.keySetting,
            SettingKey.EMAIL.keySetting
        )
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
                    coroutineScope.launch { drawerState.close() }
                }
            )
        },
        drawerState = drawerState
    ) {
        val context: Context = LocalContext.current
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
                    currentRoute = navController.currentBackStackEntry?.destination?.route
                        ?: Routes.ADMIN_DASHBOARD,
                    onItemClick = { route -> navController.navigate(route) }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                val app = LocalContext.current.applicationContext as AuraVindexApp
                val isConnected by app.networkLiveData.observeAsState(true)
                ConnectionAlert(isConnected)

                when {
                    objectName.isNullOrBlank() -> {
                        Text(
                            text = "Admin Dashboard",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )

                        val dashboardItems = listOf(
                            Triple("Books", "book", Icons.Default.Book),
                            Triple("Plans", "plan", Icons.Default.List),
                            Triple("Users", "user", Icons.Default.Person),
                            Triple("Loans", "loan", Icons.Default.AttachMoney),
                            Triple("Active Plans", "active_plan", Icons.Default.CalendarToday),
                            Triple("Audit Logs", "audit_log", Icons.Default.History),
                            Triple("Notifications", "notification", Icons.Default.Notifications)
                        )

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(dashboardItems) { (title, route, icon) ->
                                DashboardCard(title = title, icon = icon) {
                                    navController.navigate("admin_dashboard/$route")
                                }
                            }
                        }
                    }

                    objectId.isNullOrBlank() -> {
                        when (objectName?.lowercase()) {
                            AdminDashboardObject.BOOK.name.lowercase() -> {
                                ObserveTokenExpiration(bookViewModel, navController, localSettingViewModel)
                                ObserveInsufficientPermissions(bookViewModel, navController)
                                val books by bookViewModel.books.collectAsState()
                                LaunchedEffect(Unit) {
                                    bookViewModel.loadBooks(showDuplicates = true, showLents = true)
                                }
                                AdminBookTable(navController, books ?: emptyList(), context)
                            }
                            AdminDashboardObject.PLAN.name.lowercase() -> {
                                ObserveTokenExpiration(planViewModel, navController, localSettingViewModel)
                                ObserveInsufficientPermissions(planViewModel, navController)
                                val plans by planViewModel.plans.observeAsState()
                                LaunchedEffect(Unit) { planViewModel.loadPlans() }
                                AdminPlanTable(navController, plans ?: emptyList())
                            }
                            AdminDashboardObject.USER.name.lowercase() -> {
                                ObserveTokenExpiration(userViewModel, navController, localSettingViewModel)
                                ObserveInsufficientPermissions(userViewModel, navController)
                                ObserveError(userViewModel)
                                val users by userViewModel.users.observeAsState()
                                LaunchedEffect(Unit) {
                                    userViewModel.getUsers(
                                        localSettings.getOrDefault(SettingKey.TOKEN.keySetting, "")
                                    )
                                }
                                AdminUserTable(navController, users ?: emptyList())
                            }
                            AdminDashboardObject.LOAN.name.lowercase() -> {
                                ObserveTokenExpiration(loanViewModel, navController, localSettingViewModel)
                                ObserveInsufficientPermissions(loanViewModel, navController)
                                ObserveError(loanViewModel)
                                val loans by loanViewModel.loans.collectAsState()
                                LaunchedEffect(Unit) {
                                    loanViewModel.loadLoans(
                                        localSettings.getOrDefault(SettingKey.TOKEN.keySetting, "")
                                    )
                                }
                                AdminLoanTable(navController, loans ?: emptyList())
                            }
                            AdminDashboardObject.ACTIVE_PLAN.name.lowercase() -> {
                                ObserveTokenExpiration(activePlanViewModel, navController, localSettingViewModel)
                                ObserveInsufficientPermissions(activePlanViewModel, navController)
                                ObserveError(activePlanViewModel)
                                val activePlans by activePlanViewModel.activePlans.observeAsState()
                                LaunchedEffect(Unit) {
                                    activePlanViewModel.loadActivePlans(
                                        localSettings.getOrDefault(SettingKey.TOKEN.keySetting, "")
                                    )
                                }
                                AdminActivePlanTable(navController, activePlans ?: emptyList())
                            }
                            AdminDashboardObject.AUDIT_LOG.name.lowercase() -> {
                                ObserveTokenExpiration(auditLogViewModel, navController, localSettingViewModel)
                                ObserveInsufficientPermissions(auditLogViewModel, navController)
                                ObserveError(auditLogViewModel)
                                val auditLogs by auditLogViewModel.auditLogs.observeAsState()
                                LaunchedEffect(Unit) {
                                    auditLogViewModel.getAuditLogs(
                                        localSettings.getOrDefault(SettingKey.TOKEN.keySetting, "")
                                    )
                                }
                                AdminAuditLogTable(navController, auditLogs ?: emptyList())
                            }
                            AdminDashboardObject.NOTIFICATION.name.lowercase() -> {
                                ObserveTokenExpiration(notificationViewModel, navController, localSettingViewModel)
                                ObserveInsufficientPermissions(notificationViewModel, navController)
                                ObserveError(notificationViewModel)
                                val notifications by notificationViewModel.notifications.observeAsState()
                                LaunchedEffect(Unit) {
                                    notificationViewModel.loadNotifications(
                                        localSettings.getOrDefault(SettingKey.TOKEN.keySetting, "")
                                    )
                                }
                                AdminNotificationTable(navController, notifications ?: emptyList())
                            }
                            else -> {
                                Text("Unknown object", color = Color.Red)
                            }
                        }
                    }

                    else -> {
                        when (objectName?.lowercase()) {
                            AdminDashboardObject.USER.name.lowercase() -> {
                                ObserveTokenExpiration(userViewModel, navController, localSettingViewModel)
                                ObserveInsufficientPermissions(userViewModel, navController)
                                ObserveError(userViewModel)
                                AdminUserCard(navController, userViewModel, localSettingViewModel, objectId)
                            }
                            AdminDashboardObject.LOAN.name.lowercase() -> {
                                ObserveTokenExpiration(loanViewModel, navController, localSettingViewModel)
                                ObserveInsufficientPermissions(loanViewModel, navController)
                                AdminLoanCard(navController, loanViewModel, localSettingViewModel, objectId)
                            }
                            AdminDashboardObject.PLAN.name.lowercase() -> {
                                ObserveTokenExpiration(planViewModel, navController, localSettingViewModel)
                                ObserveInsufficientPermissions(planViewModel, navController)
                                AdminPlanCard(navController, planViewModel, objectId)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(180.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg),
                contentDescription = "Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}