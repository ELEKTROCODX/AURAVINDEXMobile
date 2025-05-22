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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.viewmodels.BookCollectionViewModel
import com.elektro24team.auravindex.viewmodels.BookViewModel
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.PlanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    planViewModel: PlanViewModel,
    bookCollectionViewModel: BookCollectionViewModel,
    localSettingViewModel: LocalSettingViewModel
) {
    val colors = MaterialTheme.colorScheme
    var isReadyToNavigate by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        localSettingViewModel.loadSetting(SettingKey.DARK_MODE.keySetting)
        localSettingViewModel.loadSetting(SettingKey.LANGUAGE.keySetting)
        localSettingViewModel.loadSetting(SettingKey.LAST_LOGIN.keySetting)
        localSettingViewModel.loadUserSettings()

        bookViewModel.loadBooks(showDuplicates = true, showLents = true)
        bookViewModel.fetchLatestReleases()
        planViewModel.loadPlans()
        bookCollectionViewModel.loadBookCollections()
    }
    LaunchedEffect(Unit) {
        val keys = arrayOf(
            SettingKey.DARK_MODE.keySetting,
            SettingKey.LANGUAGE.keySetting,
            SettingKey.LAST_LOGIN.keySetting
        )
        val loaded = localSettingViewModel.loadSettings(*keys)

        if (loaded[SettingKey.DARK_MODE.keySetting].isNullOrBlank()) {
            localSettingViewModel.saveSetting(SettingKey.DARK_MODE.keySetting, "false")
        }

        if (loaded[SettingKey.LANGUAGE.keySetting].isNullOrBlank()) {
            localSettingViewModel.saveSetting(SettingKey.LANGUAGE.keySetting, "English")
        }

        localSettingViewModel.saveSetting(
            SettingKey.LAST_LOGIN.keySetting,
            System.currentTimeMillis().toString()
        )

        isReadyToNavigate = true
    }

    Box(
        modifier = Modifier
            .clickable {
                if(isReadyToNavigate) {
                    navController.navigate(Routes.LOGIN) {
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
