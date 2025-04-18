package com.elektro24team.auravindex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.elektro24team.auravindex.ui.theme.AppTheme
import com.elektro24team.auravindex.ui.theme.MediumPadding
import com.elektro24team.auravindex.ui.theme.White


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                LoadingScreen(
                    modifier = Modifier.fillMaxSize(),
                    onContinue = {
                        //NAVEGACION A SGIGUIENTE SCREEM
                    }
                )
            }
        }
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    onContinue: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable { onContinue() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_main),
            contentDescription = "Fondo de Loading",
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
                color = White,
                style = MaterialTheme.typography.titleLarge
            )


            Image(
                painter = painterResource(id = R.drawable.logo_app),
                contentDescription = "Logo de Aura Vindex",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f)
            )

            Text(
                text = "Tap to continue",
                color = White,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    AppTheme {
        LoadingScreen(modifier = Modifier.fillMaxSize(), onContinue = {})
    }
}
