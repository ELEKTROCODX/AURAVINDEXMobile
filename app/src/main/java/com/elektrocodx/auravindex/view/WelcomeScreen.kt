package com.elektrocodx.auravindex.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.elektrocodx.auravindex.AuraVindexApp
import com.elektrocodx.auravindex.R
import com.elektrocodx.auravindex.navigation.Routes
import com.elektrocodx.auravindex.ui.theme.MediumPadding
import com.elektrocodx.auravindex.utils.enums.SettingKey
import com.elektrocodx.auravindex.utils.functions.isLoggedIn
import com.elektrocodx.auravindex.viewmodels.ActivePlanViewModel
import com.elektrocodx.auravindex.viewmodels.BookCollectionViewModel
import com.elektrocodx.auravindex.viewmodels.BookViewModel
import com.elektrocodx.auravindex.viewmodels.LocalSettingViewModel
import com.elektrocodx.auravindex.viewmodels.PlanViewModel
import com.elektrocodx.auravindex.viewmodels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    planViewModel: PlanViewModel,
    bookCollectionViewModel: BookCollectionViewModel,
    activePlanViewModel: ActivePlanViewModel,
    userViewModel: UserViewModel,
    localSettingViewModel: LocalSettingViewModel
) {
    val bookCollections by bookCollectionViewModel.bookCollections.observeAsState()
    val activePlan by activePlanViewModel.activePlan.observeAsState()
    val colors = MaterialTheme.colorScheme
    val settings by localSettingViewModel.settings.collectAsState()
    var isReadyToNavigate by remember { mutableStateOf(false) }
    val app = LocalContext.current.applicationContext as AuraVindexApp
    val isConnected by app.networkLiveData.observeAsState(true)
    LaunchedEffect(Unit) {
        val keys = arrayOf(
            SettingKey.DARK_MODE.keySetting,
            SettingKey.LANGUAGE.keySetting,
            SettingKey.LAST_LOGIN.keySetting
        )
        val loaded = localSettingViewModel.loadSettings(*keys)
        localSettingViewModel.loadUserSettings()
        if (loaded[SettingKey.DARK_MODE.keySetting].isNullOrBlank()) {
            localSettingViewModel.saveSetting(SettingKey.DARK_MODE.keySetting, "false")
        }
        if (loaded[SettingKey.LANGUAGE.keySetting].isNullOrBlank()) {
            localSettingViewModel.saveSetting(SettingKey.LANGUAGE.keySetting, "English")
        }
        isReadyToNavigate = true
    }
    LaunchedEffect(activePlan) {
        if (activePlan != null && isLoggedIn(settings)) {
            localSettingViewModel.saveSetting(SettingKey.ACTIVE_PLAN.keySetting, activePlan?.plan?._id.toString())
            localSettingViewModel.saveSetting(SettingKey.ACTIVE_PLAN_ID.keySetting, activePlan?._id.toString())
            localSettingViewModel.saveSetting(SettingKey.ACTIVE_PLAN_ENDING_DATE.keySetting, activePlan?.ending_date.toString())
        }
    }
    LaunchedEffect(settings) {
        if (isLoggedIn(settings)) {
            Log.d("AVDEBUG", "IS LOGGED IN")
            //bookCollectionViewModel.loadBookCollections()
            activePlanViewModel.loadActivePlanByUserId(
                settings[SettingKey.TOKEN.keySetting].toString(),
                settings[SettingKey.ID.keySetting].toString()
            )
            userViewModel.getMyUserById(
                settings[SettingKey.TOKEN.keySetting].toString(),
                settings[SettingKey.ID.keySetting].toString()
            )
            localSettingViewModel.loadUserSettings()
        } else {
            //Log.d("AVDEBUG", "IS NOT LOGGED IN")
        }
    }
    LaunchedEffect(bookCollections) {
        if(isLoggedIn(settings)) {
            bookViewModel.loadBooks(showDuplicates = true, showLents = true)
            bookViewModel.fetchLatestReleases()
            planViewModel.loadPlans()
        }
    }
    Box(
        modifier = Modifier
            .clickable {
                if(isReadyToNavigate) {
                   if(isLoggedIn(settings)) {
                       navController.navigate(Routes.MAIN) {
                           popUpTo(Routes.WELCOME) {
                               inclusive = true
                           }
                       }
                   } else {
                       navController.navigate(Routes.LOGIN) {
                           popUpTo(Routes.WELCOME) {
                               inclusive = true
                           }
                       }
                   }
                }
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = "image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MediumPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(isReadyToNavigate) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_app),
                        contentDescription = "Aura Vindex's logo",
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .aspectRatio(1f)
                    )

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
