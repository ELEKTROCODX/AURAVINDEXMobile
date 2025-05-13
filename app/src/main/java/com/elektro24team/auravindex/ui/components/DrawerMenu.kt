package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.rememberLocalSettingViewModel

//MENU HAMBURGUESA DE TIPO DRAWER O CAJON
@Composable
fun DrawerMenu(onItemSelected: (String) -> Unit) {
    val colors = MaterialTheme.colorScheme
    val localSettingsViewModel = rememberLocalSettingViewModel()
    val localSettings by localSettingsViewModel.settings.collectAsState()
    LaunchedEffect(Unit) {
        localSettingsViewModel.loadSetting(SettingKey.ID.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.EMAIL.keySetting)
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

        val menuItems = listOf(
            "Profile" to "profile",
            /*"Notifications" to "notifications",*/
            "Terms of Services" to "terms",
            "Privacy Policy" to "privacy",
            "Team" to "team",
            "Settings" to "settings"
        )

        menuItems.forEach { (label, action) ->
            Text(
                text = label,
                color = colors.onPrimary,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clickable { onItemSelected(action) }
            )
        }

        /*
        * Check if user is logged in
        * */
        if(localSettings.getOrDefault(SettingKey.ID.keySetting, "").isNotEmpty()) {
            Text(
                text = "Log out",
                color = colors.secondary,
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clickable { onItemSelected("signout") }
            )
        }
    }
}
