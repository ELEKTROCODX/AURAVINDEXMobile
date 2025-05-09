package com.elektro24team.auravindex.view

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
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController
) {
    val colors = MaterialTheme.colorScheme
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val localSettingsViewModel = rememberLocalSettingViewModel()
    val localSettings by localSettingsViewModel.settings.collectAsState()
    LaunchedEffect(Unit) {
        localSettingsViewModel.loadSetting(SettingKey.DARK_MODE.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.LANGUAGE.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.LAST_LOGIN.keySetting)
    }
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

                        // Settings
                        Text("Settings: ", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.padding(16.dp))
                        Column(
                            modifier = Modifier.padding(16.dp).fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Dark mode: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold)
                                )
                                Switch(
                                    checked = localSettings.getOrDefault(SettingKey.DARK_MODE.keySetting, "false").toBoolean(),
                                    onCheckedChange = {
                                        localSettingsViewModel.saveSetting(SettingKey.DARK_MODE.keySetting, it.toString())
                                    }
                                )

                            }
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Language: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = localSettings.getOrDefault(SettingKey.LANGUAGE.keySetting, "English")
                                )

                            }
                            Row(
                                modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Last login: ",
                                    style = TextStyle(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = localSettings.getOrDefault(SettingKey.LAST_LOGIN.keySetting, System.currentTimeMillis()).toString()
                                )

                                /*val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                formatter.timeZone = TimeZone.getTimeZone("UTC-6")
                                Text(
                                    text = formatter.format(localSettings.getOrDefault(SettingKey.LAST_LOGIN.keySetting, System.currentTimeMillis()))
                                )*/

                            }
                        }
                    }
                }
            }
        )
    }
}
