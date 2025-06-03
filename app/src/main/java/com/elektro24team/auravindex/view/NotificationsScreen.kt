package com.elektro24team.auravindex.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektro24team.auravindex.AuraVindexApp
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.ConnectionAlert
import com.elektro24team.auravindex.ui.components.DrawerMenu
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.ui.components.TopBar
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.hamburguerMenuNavigator
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.unit.Dp
import com.elektro24team.auravindex.ui.components.NotificationCard
import com.elektro24team.auravindex.utils.functions.isNotificationRecent
import com.elektro24team.auravindex.viewmodels.NotificationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    navController: NavController,
    localSettingViewModel: LocalSettingViewModel,
    userViewModel: UserViewModel,
    notificationViewModel: NotificationViewModel
) {
    MaterialTheme.colorScheme
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val userNotifications by notificationViewModel.userNotifications.observeAsState()
    val localSettings by localSettingViewModel.settings.collectAsState()
    val recentNotifications = userNotifications?.filter { isNotificationRecent(it.createdAt) }
    LaunchedEffect(Unit) {
        localSettingViewModel.loadUserSettings()
        notificationViewModel.loadUserNotifications(
            token = localSettings.getOrDefault(SettingKey.TOKEN.keySetting, ""),
            userId = localSettings.getOrDefault(SettingKey.ID.keySetting, "")
        )
    }
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
                    onItemClick = { route -> navController.navigate(route) }
                )
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFEDE7F6), Color(0xFFD1C4E9))))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        val app = context.applicationContext as AuraVindexApp
                        val isConnected by app.networkLiveData.observeAsState(true)
                        ConnectionAlert(isConnected)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Card(
                                modifier = Modifier.fillMaxSize(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                shape = MaterialTheme.shapes.medium,
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Notifications",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )
                                }
                                if(recentNotifications?.isNullOrEmpty() == false) {
                                    LazyColumn {
                                        items(recentNotifications?.size ?: 0) {
                                            NotificationCard(
                                                notification = userNotifications!![it],
                                                notificationViewModel = notificationViewModel,
                                                localSettingViewModel = localSettingViewModel,
                                                navController = navController
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "You don't have recent notifications.",
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}