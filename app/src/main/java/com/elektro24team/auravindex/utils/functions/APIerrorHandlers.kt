package com.elektro24team.auravindex.utils.functions

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.utils.enums.SettingKey
import com.elektro24team.auravindex.viewmodels.LocalSettingViewModel
import com.elektro24team.auravindex.viewmodels.base.BaseViewModel

object APIerrorHandlers {
    fun tokenExpirationHandler(
        context: Context,
        navController: NavController,
        localSettingViewModel: LocalSettingViewModel
    ) {
        Toast.makeText(context, "Session expired, please login again.", Toast.LENGTH_LONG).show()
        localSettingViewModel.clearSetting(SettingKey.TOKEN.keySetting)
        localSettingViewModel.clearSetting(SettingKey.EMAIL.keySetting)
        localSettingViewModel.clearSetting(SettingKey.ID.keySetting)
        localSettingViewModel.clearSetting(SettingKey.ROLE_NAME.keySetting)
        localSettingViewModel.clearSetting(SettingKey.ROLE_ID.keySetting)
        navController.navigate(Routes.LOGIN) {
            popUpTo(Routes.MAIN) { inclusive = true }
        }
    }
    fun insufficientPermissionsHandler(
        context: Context,
        navController: NavController
    ) {
        Toast.makeText(context, "You don't have permission to perform this action.", Toast.LENGTH_LONG).show()
        navController.navigate(Routes.MAIN) {
            popUpTo(Routes.MAIN) { inclusive = true }
        }

    }
    @Composable
    fun ObserveTokenExpiration(
        viewModel: BaseViewModel,
        navController: NavController,
        localSettingViewModel: LocalSettingViewModel
    ) {
        val tokenExpired = viewModel.tokenExpired.observeAsState()
        val context = LocalContext.current

        LaunchedEffect(tokenExpired.value) {
            if (tokenExpired.value == true) {
                tokenExpirationHandler(context, navController, localSettingViewModel)
            }
        }
    }
    @Composable
    fun ObserveInsufficentPermissions(
        viewModel: BaseViewModel,
        navController: NavController
    ) {
        val insufficientPermissions = viewModel.insufficentPermissions.observeAsState()
        val context = LocalContext.current

        LaunchedEffect(insufficientPermissions.value) {
            if (insufficientPermissions.value == true) {
                insufficientPermissionsHandler(context, navController)
            }
        }
    }
}

