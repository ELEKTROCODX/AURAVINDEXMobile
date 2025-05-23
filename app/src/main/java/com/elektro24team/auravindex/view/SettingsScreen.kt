package com.elektro24team.auravindex.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    localSettingViewModel: LocalSettingViewModel
) {
    val colors = MaterialTheme.colorScheme
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val localSettings by localSettingViewModel.settings.collectAsState()
    LaunchedEffect(Unit) {
        localSettingViewModel.loadSetting(SettingKey.DARK_MODE.keySetting)
        localSettingViewModel.loadSetting(SettingKey.LANGUAGE.keySetting)
        localSettingViewModel.loadSetting(SettingKey.LAST_LOGIN.keySetting)
        localSettingViewModel.loadSetting(SettingKey.RECEIVE_PUSH_NOTIFICATIONS.keySetting)
        localSettingViewModel.loadSetting(SettingKey.RECEIVE_EMAIL_NOTIFICATIONS.keySetting)
        localSettingViewModel.loadSetting(SettingKey.RECEIVE_SMS_NOTIFICATIONS.keySetting)
        localSettingViewModel.loadSetting(SettingKey.ID.keySetting)
        localSettingViewModel.loadSetting(SettingKey.EMAIL.keySetting)
        localSettingViewModel.loadSetting(SettingKey.TOKEN.keySetting)
        localSettingViewModel.loadSetting(SettingKey.ROLE_NAME.keySetting)
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
                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: "main",
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

                        val app = context.applicationContext as AuraVindexApp
                        val isConnected by app.networkLiveData.observeAsState(true)
                        ConnectionAlert(isConnected)

                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                        ) {
                            // Settings
                            Text(
                                text = "Settings: ",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Dark mode: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Switch(
                                    checked = localSettings.getOrDefault(SettingKey.DARK_MODE.keySetting, "false").toBoolean(),
                                    onCheckedChange = {
                                        localSettingViewModel.saveSetting(SettingKey.DARK_MODE.keySetting, it.toString())
                                    }
                                )

                            }
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Language: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Text(
                                    text = localSettings.getOrDefault(
                                        SettingKey.LANGUAGE.keySetting,
                                        "English"
                                    )
                                )
                            }
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Receive push notifications: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Switch(
                                    checked = localSettings.getOrDefault(SettingKey.RECEIVE_PUSH_NOTIFICATIONS.keySetting, "false").toBoolean(),
                                    onCheckedChange = {
                                        localSettingViewModel.saveSetting(SettingKey.RECEIVE_PUSH_NOTIFICATIONS.keySetting, it.toString())
                                    }
                                )
                            }
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Receive email notifications: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Switch(
                                    checked = localSettings.getOrDefault(SettingKey.RECEIVE_EMAIL_NOTIFICATIONS.keySetting, "false").toBoolean(),
                                    onCheckedChange = {
                                        localSettingViewModel.saveSetting(SettingKey.RECEIVE_EMAIL_NOTIFICATIONS.keySetting, it.toString())
                                    }
                                )
                            }
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Receive SMS notifications: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Switch(
                                    checked = localSettings.getOrDefault(SettingKey.RECEIVE_SMS_NOTIFICATIONS.keySetting, "false").toBoolean(),
                                    onCheckedChange = {
                                        localSettingViewModel.saveSetting(SettingKey.RECEIVE_SMS_NOTIFICATIONS.keySetting, it.toString())
                                    }
                                )
                            }
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Last login: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
                                formatter.timeZone = TimeZone.getTimeZone("UTC-6")
                                val lastLoginMillis = localSettings.getOrDefault(
                                    SettingKey.LAST_LOGIN.keySetting,
                                    System.currentTimeMillis().toString()
                                ).toLongOrNull() ?: System.currentTimeMillis()
                                val formattedDate = Instant.ofEpochMilli(lastLoginMillis)
                                    .atZone(ZoneId.of("America/El_Salvador"))
                                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                                Text(
                                    text = formattedDate,
                                )
                            }
                            // User local data (temporarily displayed for testing purposes)
                            Spacer(modifier = Modifier.padding(16.dp))
                            Text(
                                text = "User local data (temp): ",
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "User ID: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Text(
                                    text = if ((localSettings.containsKey(SettingKey.ID.keySetting)) && (localSettings[SettingKey.ID.keySetting] != "")) localSettings[SettingKey.ID.keySetting] ?: "N/A" else "N/A",
                                )
                            }
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "User email: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Text(
                                    text = if ((localSettings.containsKey(SettingKey.EMAIL.keySetting)) && (localSettings[SettingKey.EMAIL.keySetting] != "")) localSettings[SettingKey.EMAIL.keySetting] ?: "N/A" else "N/A",
                                )
                            }
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "User role: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Text(
                                    text = if ((localSettings.containsKey(SettingKey.ROLE_NAME.keySetting)) && (localSettings[SettingKey.ROLE_NAME.keySetting] != "")) localSettings[SettingKey.ROLE_NAME.keySetting] ?: "N/A" else "N/A",
                                )
                            }
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "User token: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.align(Alignment.CenterVertically)
                                )
                                Text(
                                    text = if ((localSettings.containsKey(SettingKey.TOKEN.keySetting)) && (localSettings[SettingKey.TOKEN.keySetting] != "")) localSettings[SettingKey.TOKEN.keySetting] ?: "N/A" else "N/A",
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}