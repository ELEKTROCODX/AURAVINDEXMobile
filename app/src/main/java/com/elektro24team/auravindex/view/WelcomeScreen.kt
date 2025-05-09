package com.elektro24team.auravindex.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.elektro24team.auravindex.ui.theme.MediumPadding
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.utils.rememberLocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    navController: NavController
) {
    val colors = MaterialTheme.colorScheme
    val localSettingsViewModel: LocalSettingViewModel = rememberLocalSettingViewModel()
    var isReadyToNavigate by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        localSettingsViewModel.loadSetting(SettingKey.DARK_MODE.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.LANGUAGE.keySetting)
        localSettingsViewModel.loadSetting(SettingKey.LAST_LOGIN.keySetting)
    }
    LaunchedEffect(Unit) {
        val keys = arrayOf(
            SettingKey.DARK_MODE.keySetting,
            SettingKey.LANGUAGE.keySetting,
            SettingKey.LAST_LOGIN.keySetting
        )
        val loaded = localSettingsViewModel.loadSettings(*keys)

        if (loaded[SettingKey.DARK_MODE.keySetting].isNullOrBlank()) {
            localSettingsViewModel.saveSetting(SettingKey.DARK_MODE.keySetting, "false")
        }

        if (loaded[SettingKey.LANGUAGE.keySetting].isNullOrBlank()) {
            localSettingsViewModel.saveSetting(SettingKey.LANGUAGE.keySetting, "English")
        }

        localSettingsViewModel.saveSetting(
            SettingKey.LAST_LOGIN.keySetting,
            System.currentTimeMillis().toString()
        )

        isReadyToNavigate = true
    }

    Box(
        modifier = Modifier
            .clickable {
                if(isReadyToNavigate) {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.WELCOME) {
                            inclusive = true
                        }
                    }
                }
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_welcome),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MediumPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "WELCOME TO AURA VINDEX",
                style = MaterialTheme.typography.titleLarge,
                color = colors.onPrimary
            )

            Image(
                painter = painterResource(id = R.drawable.logo_app),
                contentDescription = "Aura Vindex's logo",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f)
            )
            if(isReadyToNavigate) {
                Text(
                    text = "Tap to continue...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colors.onPrimary
                )
            } else {
                CircularProgressIndicator(
                    color = colors.onPrimary
                )
            }
        }
    }
}
