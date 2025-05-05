package com.elektro24team.auravindex.ui.theme

import android.app.Application
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

private val LightColors = lightColorScheme(
    primary = PurpleC,
    secondary = OrangeC,
    background = WhiteC,
    surface = WhiteC,
    onPrimary = WhiteC,
    onSecondary = BrownC,
    onBackground = BlackC,
    onSurface = BlackC
)
private val DarkColorScheme = darkColorScheme(
    primary = Purple,
    secondary = Orange,
    background = Black,
    surface = Black,
    onPrimary = White,
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    /*
    * Get local settings from local database
    * */
    /*val viewModel: LocalSettingViewModel = viewModel(factory = LocalSettingViewModelFactory(LocalContext.current.applicationContext as Application))
    val localsettings = viewModel.allLocalSettings.observeAsState(initial = emptyList())
    var localsetting = localsettings.value.firstOrNull()
    var localColorScheme: ColorScheme? = null
    if (localsetting != null) {
        viewModel.insert(LocalSetting(userId = "", userEmail = "", lastLoggedIn = System.currentTimeMillis().toLong(), language = "EN", darkMode = false))
    }
    localsetting = localsettings.value.firstOrNull()
    if(localsetting != null && localsetting.darkMode) {
        localColorScheme = DarkColorScheme
    } else {
        localColorScheme = LightColors
    }*/
    var localColorScheme = LightColors
    MaterialTheme(
        colorScheme = localColorScheme,
        typography = AppTypography,
        content = content
    )
}