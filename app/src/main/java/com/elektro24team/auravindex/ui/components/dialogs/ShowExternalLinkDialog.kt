package com.elektro24team.auravindex.ui.components.dialogs

import android.content.Context
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.elektro24team.auravindex.utils.functions.openLink

@Composable
fun ShowExternalLinkDialog(
    showDialog: MutableState<Boolean>,
    context: Context,
    url: String
) {
    if(showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Open Link") },
            text = { Text("You are about to leave the app and open:\n$url") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog.value = false
                    openLink(context, url)
                }) {
                    Text("Continue")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}