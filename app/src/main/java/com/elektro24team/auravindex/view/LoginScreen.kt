package com.elektro24team.auravindex.view


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elektro24team.auravindex.AuraVindexApp
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.ui.components.ConnectionAlert
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektro24team.auravindex.utils.objects.AuthPrefsHelper
import com.elektro24team.auravindex.utils.objects.FcmTokenUploader.checkAndSyncFcmToken
import com.elektro24team.auravindex.viewmodels.ActivePlanViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.AuthViewModel
import com.elektro24team.auravindex.viewmodels.UserViewModel
import kotlin.math.log


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    activePlanViewModel: ActivePlanViewModel,
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
            localSettingViewModel.clearUserSettings()
            AuthPrefsHelper.saveAuthToken(context, loginResult!!)
            userViewModel.getUserByEmail(loginResult!!, userEmail.value)
        }
    }
    LaunchedEffect(user) {
        if (user != null) {
            AuthPrefsHelper.saveUserId(context, user?._id.toString())
            localSettingViewModel.saveSetting(SettingKey.TOKEN.keySetting, loginResult!!)
            localSettingViewModel.saveSetting(SettingKey.EMAIL.keySetting, userEmail.value)
            localSettingViewModel.saveSetting(SettingKey.ID.keySetting, user?._id.toString())
            localSettingViewModel.saveSetting(SettingKey.PROFILE_IMAGE.keySetting, user?.user_img.toString())
            localSettingViewModel.saveSetting(SettingKey.ROLE_ID.keySetting, user?.role?._id.toString())
            localSettingViewModel.saveSetting(SettingKey.ROLE_NAME.keySetting, user?.role?.name.toString())
            checkAndSyncFcmToken(context)
            activePlanViewModel.loadActivePlanByUserId(
                loginResult!!,
                user?._id.toString()
            )
            userViewModel.getUserById(
                token = loginResult!!,
                userId = user?._id.toString()
            )
            userEmail.value = ""
            userPassword.value = ""
            authViewModel.loginResult.value = ""
            Toast.makeText(context, "Successfully logged in.", Toast.LENGTH_SHORT).show()
            localSettingViewModel.saveSetting(SettingKey.LAST_LOGIN.keySetting, System.currentTimeMillis().toString())
            navController.navigate(Routes.MAIN)
        }
    }
    Scaffold(
        containerColor = Color(0xFFEDE7F6),
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                val app = LocalContext.current.applicationContext as AuraVindexApp
                val isConnected by app.networkLiveData.observeAsState(true)
                ConnectionAlert(isConnected)
                Card(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Aura Vindex's logo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .background(Color(0xFF572365))
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Welcome Back!",
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF572365)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        OutlinedTextField(
                            value = userEmail.value,
                            onValueChange = { userEmail.value = it },
                            label = { Text("Email") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = userPassword.value,
                            onValueChange = { userPassword.value = it },
                            label = { Text("Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                authViewModel.login(userEmail.value.trim(), userPassword.value)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF572365))
                        ) {
                            Text("Login", color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "No account? Sign Up now!",
                            modifier = Modifier.clickable {
                                navController.navigate(Routes.SIGNUP)
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF572365)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Divider(color = Color(0xFFCCCCCC), thickness = 1.dp)

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { navController.navigate(Routes.MAIN) },
                            modifier = Modifier
                                .height(48.dp),
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8C5E4D))
                        ) {
                            Text("Skip login", color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    )

}
