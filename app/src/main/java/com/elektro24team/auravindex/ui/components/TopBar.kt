package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.viewmodels.NotificationViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    drawerState: DrawerState,
) {
    val scope = rememberCoroutineScope()

    val notificationViewModel: NotificationViewModel = viewModel()

    val hasUnreadNotifications = notificationViewModel.notification.value

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
                                contentDescription = "Menú",
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
                        val notificationIcon = if (hasUnreadNotifications?.is_read ?: false) {
                            Icons.Filled.NotificationsActive
                        } else {
                            Icons.Filled.Notifications
                        }

                        Icon(
                            imageVector = notificationIcon,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
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
