package com.elektro24team.auravindex.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.sp
import com.elektro24team.auravindex.R

val Rubik = FontFamily(
    Font(R.font.rubik_regular),
    Font(R.font.rubik_regular, weight = FontWeight.Bold)
)

val Oswald = FontFamily(
    Font(R.font.oswald_regular),
    Font(R.font.oswald_regular, weight = FontWeight.Bold)
)

val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Rubik,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Oswald,
        fontSize = 26.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Rubik,
        fontSize = 16.sp
    )
)