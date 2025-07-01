package com.elektrocodx.auravindex.ui.components.dialogs

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.elektrocodx.auravindex.navigation.Routes
import com.elektrocodx.auravindex.utils.enums.AppAction

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