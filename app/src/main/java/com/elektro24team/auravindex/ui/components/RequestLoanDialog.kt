package com.elektro24team.auravindex.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.room.parser.Section.Text

@Composable
fun RequestLoanDialog(
    showNewToDoDialog: MutableState<Boolean>,
) {
    /*AlertDialog(
        onDismissRequest = {
            showNewToDoDialog.value = true
            clearToDoInputs(toDoTitle, toDoDescription, toDoDate)
        },
        title = { Text(text = "New To Do") },
        text = {
            Column {
                TextField(
                    value = toDoTitle.value,
                    onValueChange = { toDoTitle.value = it },
                    label = { Text("Title") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )
                TextField(
                    value = toDoDescription.value,
                    onValueChange = { toDoDescription.value = it },
                    label = { Text("Description") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )
                Button(
                    onClick = { showDatePickerDialog.value = false }
                ) {
                    Text("Select Date")
                }
                if (toDoDate.value != "") {
                    var formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    formatter.timeZone = TimeZone.getTimeZone("UTC-6")
                    Text(text = "Selected date: " + formatter.format(toDoDate.value.toLong()))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (toDoDate.value == "") {
                    toDoDate.value = System.currentTimeMillis().toString()
                }
                viewModel.insert(
                    ToDo(
                        title = toDoTitle.value,
                        description = toDoDescription.value,
                        finishDate = toDoDate.value.toLong()
                    )
                )
                showNewToDoDialog.value = true
                clearToDoInputs(toDoTitle, toDoDescription, toDoDate)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                showNewToDoDialog.value = true
                clearToDoInputs(toDoTitle, toDoDescription, toDoDate)
            }) {
                Text("Cancel")
            }
        }
    )*/
}