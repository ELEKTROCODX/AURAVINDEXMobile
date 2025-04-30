package com.elektro24team.auravindex.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.elektro24team.auravindex.ui.theme.MediumPadding
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    navController: NavController
) {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .clickable {
                navController.navigate(Routes.MAIN) {
                    popUpTo(Routes.WELCOME) {
                        inclusive = true
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

            Text(
                text = "Tap to continue",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onPrimary
            )
        }
    }
}
