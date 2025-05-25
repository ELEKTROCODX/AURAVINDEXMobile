package com.elektro24team.auravindex.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elektro24team.auravindex.AuraVindexApp
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.ui.components.ConnectionAlert
import com.elektro24team.auravindex.viewmodels.GenderViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DatePicker
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(genderViewModel: GenderViewModel = viewModel()){
    val context = LocalContext.current
    val userName = remember { mutableStateOf("") }
    val userLastname = remember { mutableStateOf("") }
    val userEmail = remember { mutableStateOf("") }
    val userPassword = remember { mutableStateOf("") }
    val userBirthdate = remember { mutableStateOf<Long?>(null)}
    val genders by genderViewModel.genders.observeAsState(emptyList())
    var userGender by remember { mutableStateOf<String?>(null) }
    val showDatePicker = remember { mutableStateOf(false) }
    val formattedUserBirthdate = remember(userBirthdate.value) {
        userBirthdate.value?.let {
            val calendar = Calendar.getInstance().apply { timeInMillis = it }
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.format(calendar.time)
        } ?: "no date"
    }
    val userAddress = remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(Unit) {
        genderViewModel.getGendersList()
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        uri: Uri? ->
        imageUri = uri
    }

    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        Text(
                            text = "Signup page",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                                .align(Alignment.Center)
                        )
                    }
                    item {
                        TextField(
                            value = userName.value,
                            onValueChange = { userName.value = it },
                            label = { Text("Name") },
                            placeholder = { Text("name") },
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    item {
                        TextField(
                            value = userLastname.value,
                            onValueChange = { userLastname.value = it },
                            label = { Text("Last Name") },
                            placeholder = { Text("email") },
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    item {
                        TextField(
                            value = formattedUserBirthdate,
                            readOnly = true,
                            onValueChange = {},
                            label = { Text("Birth Date") },
                            placeholder = { Text("birthdate") },
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    item {
                        Button(
                            onClick = {
                                showDatePicker.value = true
                            }
                        ) {
                            Text("Select birthdate")
                        }
                        if (showDatePicker.value) {
                            val datePickerState = rememberDatePickerState(
                                initialSelectedDateMillis = userBirthdate.value
                                    ?: System.currentTimeMillis()
                            )
                            DatePickerDialog(
                                onDismissRequest = { showDatePicker.value = false },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            userBirthdate.value = datePickerState.selectedDateMillis
                                            showDatePicker.value = false
                                        }
                                    ) {
                                        Text("Accept")
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = { showDatePicker.value = false }
                                    ) {
                                        Text("Cancel")
                                    }
                                }
                            ) {
                                DatePicker(state = datePickerState)
                            }
                        }
                    }
                    item {
                        Text("Gender:")
                        genders.forEach { gender ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { userGender = gender._id }
                                    .padding(vertical = 4.dp)
                            ) {
                                RadioButton(
                                    selected = userGender == gender._id,
                                    onClick = {
                                        userGender = gender._id
                                    }
                                )
                                Text(
                                    text = gender.name,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                    item {
                        TextField(
                            value = userAddress.value,
                            onValueChange = { userAddress.value = it },
                            label = { Text("Address") },
                            placeholder = { Text("adress") },
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    item {
                        TextField(
                            value = userEmail.value,
                            onValueChange = { userEmail.value = it },
                            label = { Text("Email") },
                            placeholder = { Text("email") },
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    item {
                        TextField(
                            value = userPassword.value,
                            onValueChange = { userEmail.value = it },
                            label = { Text("Password") },
                            placeholder = { Text("password") },
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    item {
                        Button(
                            onClick = {
                                photoPickerLauncher.launch("image/")
                            }
                        ) {
                            Text("Select profile image")
                        }
                    }
                }
            }
        }
    )
}

