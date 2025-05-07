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
    onSecondary = OrangeC,
    onBackground = BlackC,
    onSurface = BlackC
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    var localColorScheme = LightColors
    MaterialTheme(
        colorScheme = localColorScheme,
        typography = AppTypography,
        content = content
    )
}