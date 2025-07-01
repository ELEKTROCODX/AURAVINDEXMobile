package com.elektrocodx.auravindex.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.elektrocodx.auravindex.R
import com.elektrocodx.auravindex.navigation.Routes
import com.elektrocodx.auravindex.utils.enums.SettingKey
import com.elektrocodx.auravindex.utils.functions.isLoggedIn
import com.elektrocodx.auravindex.utils.functions.isNotificationRecent
import com.elektrocodx.auravindex.viewmodels.LocalSettingViewModel
import com.elektrocodx.auravindex.viewmodels.NotificationViewModel
import kotlinx.coroutines.launch

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
        if(isLoggedIn(localSettings)) {
            notificationViewModel.loadUserNotifications(localSettings.getOrDefault(SettingKey.TOKEN.keySetting, ""), localSettings.getOrDefault(SettingKey.ID.keySetting, ""))
        }
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
                                        text = if (unreadNotifications > 3) "3+" else unreadNotifications.toString(),
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
