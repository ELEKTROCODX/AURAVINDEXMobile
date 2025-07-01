package com.elektro24team.auravindex.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektro24team.auravindex.AuraVindexApp
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.DrawerMenu
import com.elektro24team.auravindex.ui.components.TopBar
import com.elektro24team.auravindex.ui.components.alerts.ConnectionAlert
import com.elektro24team.auravindex.ui.components.alerts.NotLoggedInAlert
import com.elektro24team.auravindex.ui.components.cards.PlanCard
import com.elektro24team.auravindex.ui.components.dialogs.ShowExternalLinkDialog
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveInsufficientPermissions
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveSuccess
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveTokenExpiration
import com.elektro24team.auravindex.utils.functions.hamburguerMenuNavigator
import com.elektro24team.auravindex.utils.functions.isLoggedIn
import com.elektro24team.auravindex.viewmodels.ActivePlanViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.NotificationViewModel
import com.elektro24team.auravindex.viewmodels.PlanViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanScreen(
    navController: NavController,
    planViewModel: PlanViewModel,
    activePlanViewModel: ActivePlanViewModel,
    userViewModel: UserViewModel,
    notificationViewModel: NotificationViewModel,
    localSettingViewModel: LocalSettingViewModel,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    rememberCoroutineScope()
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val plans by planViewModel.plans.observeAsState(emptyList())
    val localSettings = localSettingViewModel.settings.collectAsState()
    ObserveSuccess(activePlanViewModel)
    ObserveError(activePlanViewModel)
    ObserveInsufficientPermissions(activePlanViewModel, navController)
    ObserveTokenExpiration(activePlanViewModel, navController, localSettingViewModel)
    LaunchedEffect(Unit) {
        planViewModel.loadPlans()
        activePlanViewModel.clearNotifications()
        if(isLoggedIn(localSettings.value)) {
            activePlanViewModel.loadActivePlanByUserId(
                localSettings.value.getOrDefault(SettingKey.TOKEN.keySetting, ""),
                localSettings.value.getOrDefault(SettingKey.ID.keySetting, "")
            )
        }
    }
    ModalNavigationDrawer(
        drawerContent = {
            DrawerMenu(
                navController = navController,
                currentRoute = navController.currentBackStackEntry?.destination?.route,
                userViewModel = userViewModel,
                localSettingViewModel = localSettingViewModel,
                notificationViewModel = notificationViewModel,
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
                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: Routes.PLANS,
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        val app = LocalContext.current.applicationContext as AuraVindexApp
                        val isConnected by app.networkLiveData.observeAsState(true)
                        ConnectionAlert(isConnected)
                        NotLoggedInAlert(localSettings.value)
                        Text(
                            text = "Subscription Plans",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                        )

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(plans?.size ?: 0) { index ->
                                PlanCard(
                                    plan = plans?.get(index),
                                    navController,
                                    localSettingViewModel,
                                    activePlanViewModel,
                                    notificationViewModel
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

