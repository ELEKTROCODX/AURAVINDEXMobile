package com.elektro24team.auravindex.view

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektro24team.auravindex.AuraVindexApp
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.ui.components.ConnectionAlert
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.AuthViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    localSettingViewModel: LocalSettingViewModel
) {
    val context = LocalContext.current
    val loginResult by authViewModel.loginResult.observeAsState()
    val user by userViewModel.user.observeAsState()
    val userEmail = remember { mutableStateOf("") }
    val userPassword = remember { mutableStateOf("") }
    ObserveError(authViewModel)
    LaunchedEffect(loginResult) {
        if (loginResult != null && loginResult != "") {
            localSettingViewModel.clearSettings(SettingKey.TOKEN.keySetting, SettingKey.ID.keySetting, SettingKey.EMAIL.keySetting, SettingKey.PROFILE_IMAGE.keySetting, SettingKey.ROLE_ID.keySetting, SettingKey.ROLE_NAME.keySetting)
            userViewModel.getUserByEmail(loginResult!!, userEmail.value)
        }
    }
    LaunchedEffect(user) {
        if (user != null) {
            localSettingViewModel.saveSetting(SettingKey.TOKEN.keySetting, loginResult!!)
            localSettingViewModel.saveSetting(SettingKey.EMAIL.keySetting, userEmail.value)
            localSettingViewModel.saveSetting(SettingKey.ID.keySetting, user?._id.toString())
            localSettingViewModel.saveSetting(SettingKey.PROFILE_IMAGE.keySetting, user?.user_img.toString())
            localSettingViewModel.saveSetting(SettingKey.ROLE_ID.keySetting, user?.role?._id.toString())
            localSettingViewModel.saveSetting(SettingKey.ROLE_NAME.keySetting, user?.role?.name.toString())
            userEmail.value = ""
            userPassword.value = ""
            authViewModel.loginResult.value = ""
            userViewModel.user.value = null
            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.MAIN)        }
    }
    Scaffold(
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
                            authViewModel.login(userEmail.value, userPassword.value)
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
