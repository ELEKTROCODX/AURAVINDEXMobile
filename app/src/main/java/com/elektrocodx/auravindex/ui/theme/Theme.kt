package com.elektrocodx.auravindex.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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