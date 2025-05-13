package com.elektro24team.auravindex.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
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
import com.elektro24team.auravindex.utils.hamburguerMenuNavigator
import com.elektro24team.auravindex.utils.rememberLocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    localSettingsViewModel: LocalSettingViewModel
) {
    val colors = MaterialTheme.colorScheme
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val localSettings by localSettingsViewModel.settings.collectAsState()
    LaunchedEffect(Unit) {
        localSettingsViewModel.loadSetting(SettingKey.DARK_MODE.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.LANGUAGE.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.LAST_LOGIN.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.RECEIVE_PUSH_NOTIFICATIONS.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.RECEIVE_EMAIL_NOTIFICATIONS.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.RECEIVE_SMS_NOTIFICATIONS.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.ID.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.EMAIL.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.TOKEN.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.ROLE_NAME.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.ROLE_ID.keySetting)
    }
    var listSettings = listOf<String>(
        SettingKey.DARK_MODE.keySetting,
        SettingKey.LANGUAGE.keySetting,
        SettingKey.LAST_LOGIN.keySetting,
        SettingKey.RECEIVE_PUSH_NOTIFICATIONS.keySetting,
        SettingKey.RECEIVE_EMAIL_NOTIFICATIONS.keySetting,
        SettingKey.RECEIVE_SMS_NOTIFICATIONS.keySetting,
        SettingKey.ID.keySetting,
        SettingKey.EMAIL.keySetting,
        SettingKey.TOKEN.keySetting,
        SettingKey.ROLE_NAME.keySetting,
        SettingKey.ROLE_ID.keySetting
    )
    ModalNavigationDrawer(
        drawerContent = {
            DrawerMenu(onItemSelected = { route ->
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
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // Settings
                            // User local data (temporarily displayed for testing purposes)
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                items(listSettings.size) { index ->
                                    Row(
                                        modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = listSettings[index])
                                        Text(text = localSettings[listSettings[index]] ?: "N/A")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
