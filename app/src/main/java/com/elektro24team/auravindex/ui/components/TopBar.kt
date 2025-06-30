package com.elektro24team.auravindex.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.viewmodels.NotificationViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.isNotificationRecent
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    drawerState: DrawerState,
    localSettingViewModel: LocalSettingViewModel,
    notificationViewModel: NotificationViewModel
) {
    val scope = rememberCoroutineScope()
    val localSettings by localSettingViewModel.settings.collectAsState()
    val userNotifications by notificationViewModel.userNotifications.observeAsState()
    var unreadNotifications by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        notificationViewModel.loadUserNotifications(localSettings.getOrDefault(SettingKey.TOKEN.keySetting, ""), localSettings.getOrDefault(SettingKey.ID.keySetting, ""))
    }
    LaunchedEffect(userNotifications) {
        if(userNotifications?.isNotEmpty() == true) {
            unreadNotifications = 0
            userNotifications?.forEach {
                if(!it.is_read && isNotificationRecent(it.createdAt)) {
                    unreadNotifications++
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp, max = 100.dp)
            .zIndex(100f)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Image(
                            painter = painterResource(id = R.drawable.name_app),
                            contentDescription = "App Logo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .heightIn(min = 30.dp, max = 38.dp)
                                .clickable {
                                    navController.navigate(Routes.MAIN) {
                                        popUpTo(Routes.MAIN) { inclusive = true }
                                    }
                                }
                        )
                    }

                    IconButton(onClick = {
                        navController.navigate(Routes.NOTIFICATIONS)
                    }) {
                        Box {
                            val notificationIcon = if (unreadNotifications != 0) {
                                Icons.Filled.NotificationsActive
                            } else {
                                Icons.Filled.Notifications
                            }

                            Icon(
                                imageVector = notificationIcon,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                            if (unreadNotifications > 0) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = 6.dp, y = (-4).dp)
                                        .size(16.dp)
                                        .background(Color.Red, shape = CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = unreadNotifications.toString(),
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            ),
            navigationIcon = {}
        )
    }
}
