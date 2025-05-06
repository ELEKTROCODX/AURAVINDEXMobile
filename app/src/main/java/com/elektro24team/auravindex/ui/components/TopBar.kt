package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    drawerState: DrawerState
) {
    val scope: CoroutineScope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Row {
                Text(
                    text = "AURA",
                    modifier = Modifier.clickable {
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = true }
                        }
                    },
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "VINDEX",
                    modifier = Modifier.clickable {
                        navController.navigate("main") {
                            popUpTo("main") { inclusive = true }
                        }
                    },
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menú",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.onSurface
        )
    )
}
