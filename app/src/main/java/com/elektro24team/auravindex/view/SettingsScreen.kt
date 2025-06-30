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
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import com.elektro24team.auravindex.viewmodels.NotificationViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
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
    val localSettings by localSettingViewModel.settings.collectAsState()
    LaunchedEffect(Unit) {
        localSettingViewModel.loadSetting(SettingKey.DARK_MODE.keySetting)
        localSettingViewModel.loadSetting(SettingKey.LANGUAGE.keySetting)
        localSettingViewModel.loadSetting(SettingKey.RECEIVE_PUSH_NOTIFICATIONS.keySetting)
        localSettingViewModel.loadSetting(SettingKey.RECEIVE_EMAIL_NOTIFICATIONS.keySetting)
        localSettingViewModel.loadSetting(SettingKey.RECEIVE_SMS_NOTIFICATIONS.keySetting)
        localSettingViewModel.loadUserSettings()
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
                                .verticalScroll(rememberScrollState()),
                        ) {

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Settings",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )

                                    SettingRow(
                                        label = "Dark mode:",
                                        checked = localSettings.getOrDefault(SettingKey.DARK_MODE.keySetting, "false").toBoolean(),
                                        onCheckedChange = {
                                            localSettingViewModel.saveSetting(SettingKey.DARK_MODE.keySetting, it.toString())
                                        }
                                    )

                                    SettingRow(
                                        label = "Language:",
                                        value = localSettings.getOrDefault(SettingKey.LANGUAGE.keySetting, "English")
                                    )

                                    SettingRow(
                                        label = "Receive push notifications:",
                                        checked = localSettings.getOrDefault(SettingKey.RECEIVE_PUSH_NOTIFICATIONS.keySetting, "false").toBoolean(),
                                        onCheckedChange = {
                                            localSettingViewModel.saveSetting(SettingKey.RECEIVE_PUSH_NOTIFICATIONS.keySetting, it.toString())
                                        }
                                    )

                                    SettingRow(
                                        label = "Receive email notifications:",
                                        checked = localSettings.getOrDefault(SettingKey.RECEIVE_EMAIL_NOTIFICATIONS.keySetting, "false").toBoolean(),
                                        onCheckedChange = {
                                            localSettingViewModel.saveSetting(SettingKey.RECEIVE_EMAIL_NOTIFICATIONS.keySetting, it.toString())
                                        }
                                    )

                                    SettingRow(
                                        label = "Receive SMS notifications:",
                                        checked = localSettings.getOrDefault(SettingKey.RECEIVE_SMS_NOTIFICATIONS.keySetting, "false").toBoolean(),
                                        onCheckedChange = {
                                            localSettingViewModel.saveSetting(SettingKey.RECEIVE_SMS_NOTIFICATIONS.keySetting, it.toString())
                                        }
                                    )

                                    SettingRow(
                                        label = "Last login:",
                                        value = run {
                                            val lastLoginStr = localSettings.getOrDefault(SettingKey.LAST_LOGIN.keySetting, "")
                                            if(lastLoginStr.isNotEmpty()) {
                                                val lastLoginMillis = lastLoginStr.toLongOrNull() ?: System.currentTimeMillis()
                                                val formattedDate = Instant.ofEpochMilli(lastLoginMillis)
                                                    .atZone(ZoneId.of("America/El_Salvador"))
                                                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                                                formattedDate
                                            } else {
                                                "N/A"
                                            }
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "User local data (temp)",
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )

                                    SimpleTextRow("User ID:", localSettings[SettingKey.ID.keySetting])
                                    SimpleTextRow("User email:", localSettings[SettingKey.EMAIL.keySetting])
                                    SimpleTextRow("User role:", localSettings[SettingKey.ROLE_NAME.keySetting])
                                    SimpleTextRow("Active plan ID:", localSettings[SettingKey.ACTIVE_PLAN_ID.keySetting])
                                    SimpleTextRow("Active plan name:", localSettings[SettingKey.ACTIVE_PLAN.keySetting])

                                    SimpleTextRow("Active plan ending date:", localSettings[SettingKey.ACTIVE_PLAN_ENDING_DATE.keySetting])
                                    SimpleTextRow("User token:", localSettings[SettingKey.TOKEN.keySetting])
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun SettingRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun SettingRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        Text(text = value)
    }
}
@Composable
fun SimpleTextRow(label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = TextStyle(fontWeight = FontWeight.Bold))
        Text(text = if (!value.isNullOrEmpty()) value else "N/A")
    }
}