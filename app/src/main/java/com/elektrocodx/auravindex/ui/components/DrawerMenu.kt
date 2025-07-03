package com.elektrocodx.auravindex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.elektrocodx.auravindex.R
import com.elektrocodx.auravindex.navigation.Routes
import com.elektrocodx.auravindex.utils.classes.AdminMenuItem
import com.elektrocodx.auravindex.utils.classes.DefaultMenuItem
import com.elektrocodx.auravindex.utils.constants.URLs.IMG_url
import com.elektrocodx.auravindex.utils.enums.AdminDashboardObject
import com.elektrocodx.auravindex.utils.enums.SettingKey
import com.elektrocodx.auravindex.utils.functions.isAdmin
import com.elektrocodx.auravindex.utils.functions.isInAdminDashboard
import com.elektrocodx.auravindex.utils.functions.isLoggedIn
import com.elektrocodx.auravindex.viewmodels.LocalSettingViewModel
import com.elektrocodx.auravindex.viewmodels.NotificationViewModel
import com.elektrocodx.auravindex.viewmodels.UserViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun DrawerMenu(
    navController: NavController,
    currentRoute: String?,
    onItemSelected: (String) -> Unit,
    userViewModel: UserViewModel,
    notificationViewModel: NotificationViewModel,
    localSettingViewModel: LocalSettingViewModel
) {
    val colors = MaterialTheme.colorScheme
    val localSettings by localSettingViewModel.settings.collectAsState()
    val user by userViewModel.myUser.observeAsState()
    LaunchedEffect(Unit) {
        if(isLoggedIn(localSettings)) {
            notificationViewModel.loadUserNotifications(localSettings.getOrDefault(SettingKey.TOKEN.keySetting, ""), localSettings.getOrDefault(SettingKey.ID.keySetting, ""))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(start = 16.dp, top = 48.dp, end = 16.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .clickable {
                        navController.navigate("profile")
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.Black)
                    ) {
                        GlideImage(
                            imageModel = { IMG_url + (user?.user_img ?: "") },
                            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                            modifier = Modifier.fillMaxSize(),
                            failure = {
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = "Avatar",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = if (isLoggedIn(localSettings)) "${user?.name} ${user?.last_name}".trim() else "Guest User",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color(0xFF572365),
                            fontSize = 26.sp
                        )
                    )
                }
            }
        }
        val commonModifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(48.dp)
        if (isInAdminDashboard(currentRoute)) {
            val adminMenuItems = listOf(
                AdminMenuItem("Books", Icons.AutoMirrored.Filled.MenuBook, AdminDashboardObject.BOOK),
                AdminMenuItem("Plans", Icons.AutoMirrored.Filled.List, AdminDashboardObject.PLAN),
                AdminMenuItem("Users", Icons.Default.Person, AdminDashboardObject.USER),
                AdminMenuItem("Loans", Icons.AutoMirrored.Filled.ReceiptLong, AdminDashboardObject.LOAN),
                AdminMenuItem("Active Plans", Icons.Default.PlayCircleFilled, AdminDashboardObject.ACTIVE_PLAN),
                AdminMenuItem("Audit Log", Icons.Default.Terminal, AdminDashboardObject.AUDIT_LOG),
                AdminMenuItem("Notifications", Icons.Default.Notifications, AdminDashboardObject.NOTIFICATION)
            )

            CustomDrawerItem(
                title = "Home page",
                icon = Icons.Default.Home,
                isSelected = currentRoute == Routes.MAIN,
                modifier = commonModifier,
                onClick = { navController.navigate(Routes.MAIN) }
            )

            CustomDrawerItem(
                title = "Admin page",
                icon = Icons.Default.AdminPanelSettings,
                isSelected = currentRoute == Routes.ADMIN_DASHBOARD,
                modifier = commonModifier,
                onClick = { navController.navigate(Routes.ADMIN_DASHBOARD) }
            )

            adminMenuItems.forEach { item ->
                CustomDrawerItem(
                    title = item.title,
                    icon = item.icon,
                    isSelected = currentRoute?.endsWith(item.dashboardObject.name.lowercase()) == true,
                    modifier = commonModifier,
                    onClick = {
                        onItemSelected("admin_dashboard/${item.dashboardObject.name.lowercase()}")
                    }
                )
            }
        } else {
            val menuItems = listOf(
                DefaultMenuItem("Home", Icons.Default.Home, Routes.MAIN),
                DefaultMenuItem("My Loans", Icons.AutoMirrored.Filled.LibraryBooks, Routes.LOANS),
                DefaultMenuItem("Terms of Services", Icons.Default.Newspaper, Routes.TERMS),
                DefaultMenuItem("Privacy Policy", Icons.Default.PrivacyTip, Routes.PRIVACY),
                DefaultMenuItem("Team", Icons.Default.Groups, Routes.TEAM),
                /*DefaultMenuItem("Settings", Icons.Default.Settings, Routes.SETTINGS)*/
            )

            menuItems.forEach { item ->
                CustomDrawerItem(
                    title = item.title,
                    icon = item.icon,
                    isSelected = currentRoute == item.route,
                    modifier = commonModifier,
                    onClick = { onItemSelected(item.route) }
                )
            }

            if (isAdmin(localSettings)) {
                CustomDrawerItem(
                    title = "Admin",
                    icon = Icons.Default.AdminPanelSettings,
                    isSelected = currentRoute == Routes.ADMIN_DASHBOARD,
                    modifier = commonModifier,
                    onClick = { onItemSelected(Routes.ADMIN_DASHBOARD) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val isUserLoggedIn = isLoggedIn(localSettings)
            val loginLabel = if (isUserLoggedIn) "Log out" else "Log in"
            val loginIcon = if (isUserLoggedIn) Icons.Default.ArrowBackIosNew else Icons.Default.ArrowBackIosNew
            val loginAction = if (isUserLoggedIn) "logout" else "login"

            Surface(
                onClick = { onItemSelected(loginAction) },
                shape = RoundedCornerShape(12.dp),
                color = colors.primary.copy(alpha = 0.15f),
                tonalElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 12.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = loginIcon,
                        contentDescription = loginLabel,
                        tint = colors.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = loginLabel,
                        color = colors.primary,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
fun CustomDrawerItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val backgroundColor = if (isSelected) colors.primary.copy(alpha = 0.1f) else Color.Transparent
    val contentColor = if (isSelected) colors.primary else colors.onBackground

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(backgroundColor, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = contentColor
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = contentColor,
            fontSize = 16.sp
        )
    }
}
