package com.elektrocodx.auravindex.ui.components.dialogs

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.room.parser.Section.Text
import com.elektrocodx.auravindex.viewmodels.GenderViewModel
import com.elektrocodx.auravindex.viewmodels.UserViewModel

@Composable
fun EditUserDialog (
    navController: NavController,
    showEditUserDialog: MutableState<Boolean>,
    genderViewModel: GenderViewModel,
    userViewModel: UserViewModel
) {
    val name = remember { mutableStateOf("") }
    val last_name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val biography = remember { mutableStateOf("") }
    val gender = remember { mutableStateOf("") }
    val birthdate = remember { mutableStateOf<Long?>(null) }
    val address = remember { mutableStateOf("") }
    val role = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val genders by genderViewModel.genders.observeAsState(emptyList())
    LaunchedEffect(Unit) { genderViewModel.getGendersList() }
    val photoPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            imageUri = it
        }
    AlertDialog(
        onDismissRequest = {
            showEditUserDialog.value = false
        },
        title = { Text("Edit user") },
        text = {
            Column {
            TextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text("Name") }
            )
            TextField(
                value = last_name.value,
                onValueChange = { last_name.value = it },
                label = { Text("Last name") }
            )
            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") }
            )
            TextField(
                value = biography.value,
                onValueChange = { biography.value = it },
                label = { Text("Biography") }
            )
            TextField(
                value = gender.value,
                onValueChange = { gender.value = it },
                label = { Text("Gender") }
            )
                
            TextField(
                value = address.value,
                onValueChange = { address.value = it },
                label = { Text("Address") }
            )
            TextField(
                value = role.value,
                onValueChange = { role.value = it },
                label = { Text("Role") }
            )
            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") }
            )
        } },
        confirmButton = {
            TextButton(onClick = {
            }) {
                Text("Edit")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                showEditUserDialog.value = false
            }) {
                Text("Cancel")
            }
        }
    )
}