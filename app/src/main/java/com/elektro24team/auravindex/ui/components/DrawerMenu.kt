package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.LibraryBooks
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.utils.classes.AdminMenuItem
import com.elektro24team.auravindex.utils.classes.DefaultMenuItem
import com.elektro24team.auravindex.utils.enums.AdminDashboardObject
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.functions.isAdmin
import com.elektro24team.auravindex.utils.functions.isInAdminDashboard
import com.elektro24team.auravindex.utils.functions.isLoggedIn
import com.elektro24team.auravindex.utils.functions.rememberLocalSettingViewModel

//MENU HAMBURGUESA DE TIPO DRAWER O CAJON
@Composable
fun DrawerMenu(
    navController: NavController,
    currentRoute: String?,
    onItemSelected: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val localSettingsViewModel = rememberLocalSettingViewModel()
    val localSettings by localSettingsViewModel.settings.collectAsState()
    LaunchedEffect(Unit) {
        localSettingsViewModel.loadSetting(SettingKey.ID.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.EMAIL.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.TOKEN.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.ROLE_NAME.keySetting)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.onBackground)
            .padding(start = 24.dp, top = 64.dp, end = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally // poisicion del logo
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = "Logo",
            modifier = Modifier
                .height(100.dp)
                .padding(bottom = 32.dp)
        )
        if(isInAdminDashboard(currentRoute)) {
            val adminMenuItems = listOf(
                AdminMenuItem("Books", Icons.AutoMirrored.Filled.MenuBook, AdminDashboardObject.BOOK),
                AdminMenuItem("Users", Icons.Default.Person, AdminDashboardObject.USER),
                AdminMenuItem("Loans", Icons.AutoMirrored.Filled.ReceiptLong, AdminDashboardObject.LOAN),
                AdminMenuItem("Notifications", Icons.Default.Notifications, AdminDashboardObject.NOTIFICATION),
                AdminMenuItem("Fees", Icons.Default.AttachMoney, AdminDashboardObject.FEE),
                AdminMenuItem("Plans", Icons.AutoMirrored.Filled.List, AdminDashboardObject.PLAN),
                AdminMenuItem("Active Plans", Icons.Default.PlayCircleFilled, AdminDashboardObject.ACTIVE_PLAN),
                AdminMenuItem("Audit Log", Icons.Default.Terminal, AdminDashboardObject.AUDIT_LOG)
            )
            NavigationDrawerItem(
                label = { Text(
                    text = "Home page",
                    color = colors.onPrimary
                ) },
                icon = { Icon(
                    Icons.Default.Home,
                    contentDescription = "Home page",
                    tint = colors.onPrimary) },
                selected = false,
                onClick = {
                    navController.navigate(Routes.MAIN)
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
            NavigationDrawerItem(
                label = { Text(
                    text = "Admin page",
                    color = colors.onPrimary
                ) },
                icon = { Icon(
                    Icons.Filled.AdminPanelSettings,
                    contentDescription = "Admin page",
                    tint = colors.onPrimary) },
                selected = false,
                onClick = {
                    navController.navigate(Routes.ADMIN_DASHBOARD)
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
            adminMenuItems.forEach { item ->
                NavigationDrawerItem(
                    label = { Text(
                        text = item.title,
                        color = colors.onPrimary,
                    ) },
                    icon = { Icon(
                        item.icon,
                        contentDescription = item.title,
                        tint = colors.onPrimary) },
                    selected = false,
                    onClick = {
                        onItemSelected("admin_dashboard/${item.dashboardObject.name.lowercase()}")
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        } else {
            val menuItems = listOf(
                DefaultMenuItem("Home", Icons.Default.Home, Routes.MAIN),
                DefaultMenuItem("Profile", Icons.Default.Person, Routes.PROFILE),
                DefaultMenuItem("My Loans", Icons.AutoMirrored.Filled.LibraryBooks, Routes.NOTIFICATIONS),
                DefaultMenuItem("Terms of Services", Icons.Default.Newspaper, Routes.TERMS),
                DefaultMenuItem("Privacy Policy", Icons.Filled.PrivacyTip, Routes.PRIVACY),
                DefaultMenuItem("Team", Icons.Filled.Groups, Routes.TEAM),
                DefaultMenuItem("Settings", Icons.Filled.Settings, Routes.SETTINGS)
            )
            menuItems.forEach { item ->
                NavigationDrawerItem(
                    label = { Text(
                        text = item.title,
                        color = colors.onPrimary,
                    ) },
                    icon = { Icon(
                        item.icon,
                        contentDescription = item.title,
                        tint = colors.onPrimary) },
                    selected = false,
                    onClick = {
                        onItemSelected(item.route)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
            if(isAdmin(localSettings)) {
                NavigationDrawerItem(
                    label = { Text(
                        text = "Admin",
                        color = colors.onPrimary,
                    ) },
                    icon = { Icon(
                        Icons.Filled.AdminPanelSettings,
                        contentDescription = "Admin",
                        tint = colors.onPrimary) },
                    selected = false,
                    onClick = {
                        onItemSelected(Routes.ADMIN_DASHBOARD)
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }

            /*
            * Check if user is logged in
            * */
            if(isLoggedIn(localSettings)) {
                Text(
                    text = "Log out",
                    color = colors.secondary,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clickable { onItemSelected("logout") }
                )
            } else {
                Text(
                    text = "Log in",
                    color = colors.secondary,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 12.dp)
                        .clickable { onItemSelected("login") }
                )
            }
        }
    }
}
