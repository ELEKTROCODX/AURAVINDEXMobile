package com.elektro24team.auravindex.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.elektro24team.auravindex.AuraVindexApp
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.ui.components.BottomNavBar
import com.elektro24team.auravindex.ui.components.ConnectionAlert
import com.elektro24team.auravindex.ui.components.DrawerMenu
import com.elektro24team.auravindex.ui.components.ShowExternalLinkDialog
import com.elektro24team.auravindex.ui.components.TopBar
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.hamburguerMenuNavigator
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.LoginViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    localSettingsViewModel: LocalSettingViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val showTermsDialog = remember { mutableStateOf(false) }
    val showPrivacyDialog = remember { mutableStateOf(false) }
    val showTeamDialog = remember { mutableStateOf(false) }
    val viewModel: LoginViewModel = viewModel()
    val userViewModel: UserViewModel = viewModel()
    val userResult by userViewModel.user.observeAsState()
    val loginResult by viewModel.loginResult.observeAsState()
    val userEmail = remember { mutableStateOf("") }
    val userPassword = remember { mutableStateOf("") }
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

        loginResult?.let { result ->
            result.onSuccess {
                token ->
                localSettingsViewModel.saveSetting(SettingKey.TOKEN.keySetting, token)
                //Log.d("Token ", "token recibido: $token")
                userViewModel.getUser(token,userEmail.value)
                navController.navigate(Routes.MAIN)
            }
            result.onFailure { error ->
                Log.d("error: ", error.message.toString())
                Toast.makeText(context, "Error de login: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        }

        Scaffold(
            topBar = {
                TopBar(navController = navController, drawerState = drawerState)
            },
            bottomBar = {
                BottomNavBar(
                    currentRoute = navController.currentBackStackEntry?.destination?.route ?: "login",
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
                        val app = LocalContext.current.applicationContext as AuraVindexApp
                        val isConnected by app.networkLiveData.observeAsState(true)
                        ConnectionAlert(isConnected)
                        Text(
                            text = "Login page",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = 8.dp).align(Alignment.CenterHorizontally)
                        )
                        TextField(
                            value = userEmail.value,
                            onValueChange = {userEmail.value = it},
                            label = { Text("Email")},
                            placeholder = { Text("email")},
                            modifier = Modifier.padding(16.dp)
                        )
                        TextField(
                            value = userPassword.value,
                            onValueChange = {userPassword.value = it},
                            label = { Text("Password")},
                            placeholder = { Text("password")},
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(
                            onClick = {
                                Log.d("Email: ", userEmail.value)
                                viewModel.login(userEmail.value,userPassword.value)
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = "Login",
                                modifier = Modifier.padding(vertical = 8.dp).align(Alignment.CenterVertically)
                            )
                        }
                        Button(
                            onClick = {
                                navController.navigate(Routes.MAIN)
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {

                            Text(
                                text = "Skip login",
                                modifier = Modifier.padding(vertical = 8.dp).align(Alignment.CenterVertically)
                            )
                        }
                    }
                }
            }
        )
    }
}
