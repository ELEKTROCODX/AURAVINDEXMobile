package com.elektro24team.auravindex.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.utils.enums.AppAction

/* Deprecated */
@Composable
fun MustBeLoggedInDialog (
    navController: NavController,
    action: AppAction,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Unauthorized!") },
        text = { Text("You must be logged in to ${action.appActionTitle}.") },
        confirmButton = {
            TextButton(onClick = {
                navController.navigate(Routes.LOGIN)
                onDismiss()
            }) {
                Text("Log in")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text("Cancel")
            }
        }
    )
}