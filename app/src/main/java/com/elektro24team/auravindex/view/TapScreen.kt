/*

ESTE ARCHIVO AUN NO FUNCIONA CON LOS DEMAS NO TOCAR

package com.elektro24team.auravindex.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.elektro24team.auravindex.ui.theme.AppTheme
import com.elektro24team.auravindex.ui.theme.MediumPadding

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.elektro24team.auravindex.ui.components.DrawerMenu
import com.elektro24team.auravindex.ui.theme.MediumPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun TapScreen(
    modifier: Modifier = Modifier,
    onContinue: () -> Unit
) {
    Box(
        modifier = modifier
            .clickable { onContinue() }
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
                style = MaterialTheme.typography.titleLarge
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
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
*/