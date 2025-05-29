package com.elektro24team.auravindex.view

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.parser.Section
import coil.compose.rememberAsyncImagePainter
import com.elektro24team.auravindex.R
import com.elektro24team.auravindex.navigation.Routes
import com.elektro24team.auravindex.retrofit.RegisterInfo
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveError
import com.elektro24team.auravindex.utils.functions.APIerrorHandlers.ObserveSuccess
import com.elektro24team.auravindex.utils.functions.isValidEmail
import com.elektro24team.auravindex.viewmodels.AuthViewModel
import com.elektro24team.auravindex.viewmodels.GenderViewModel
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    genderViewModel: GenderViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val registerResult by authViewModel.registerResult.observeAsState()

    // Estados de entrada del usuario
    val userName = remember { mutableStateOf("") }
    val userLastname = remember { mutableStateOf("") }
    val userEmail = remember { mutableStateOf("") }
    val userPassword = remember { mutableStateOf("") }
    val userBirthdate = remember { mutableStateOf<Long?>(null) }
    val userAddress = remember { mutableStateOf("") }
    val userBiography = remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var userGender by remember { mutableStateOf<String?>(null) }
    ObserveError(authViewModel)
    ObserveSuccess(authViewModel)
    // Estado para el DatePicker
    val showDatePicker = remember { mutableStateOf(false) }

    // Formatear fecha
    val formattedUserBirthdate = remember(userBirthdate.value) {
        userBirthdate.value?.let {
            val calendar = Calendar.getInstance().apply { timeInMillis = it }
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.format(calendar.time)
        } ?: ""
    }

    // Calcular edad
    val userAge = remember(formattedUserBirthdate) {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val birthDate = sdf.parse(formattedUserBirthdate)
            birthDate?.let {
                val birthCalendar = Calendar.getInstance().apply { time = it }
                val today = Calendar.getInstance()
                var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
                if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) age--
                age
            } ?: 0
        } catch (e: Exception) {
            0
        }
    }

    // Obtener géneros
    val genders by genderViewModel.genders.observeAsState(emptyList())
    LaunchedEffect(Unit) { genderViewModel.getGendersList() }

    // Image picker
    val photoPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            imageUri = it
        }

    // Registro observado

    // Mostrar Toast después de registrar
    LaunchedEffect(registerResult) {
        if(registerResult == true) {
            navController.navigate(Routes.LOGIN)
        }
    }
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFEDE7F6), Color(0xFFD1C4E9))
                    )
                )
                .padding(paddingValues)
        )
        Card(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Aura Vindex's logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(Color(0xFF572365))
                    )
                    Text(
                        text = "Create your account!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF572365)
                        ),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                item {
                    val textFieldModifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)

                    val fieldColors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF572365),
                        unfocusedBorderColor = Color(0xFF8C5E4D),
                        focusedLabelColor = Color(0xFF572365),
                        unfocusedLabelColor = Color(0xFF8C5E4D),
                        cursorColor = Color(0xFF572365)
                    )

                    OutlinedTextField(
                        value = userName.value,
                        onValueChange = { userName.value = it },
                        label = { Text("Name") },
                        modifier = textFieldModifier,
                        shape = RoundedCornerShape(16.dp),
                        colors = fieldColors
                    )

                    OutlinedTextField(
                        value = userLastname.value,
                        onValueChange = { userLastname.value = it },
                        label = { Text("Last Name") },
                        modifier = textFieldModifier,
                        shape = RoundedCornerShape(16.dp),
                        colors = fieldColors
                    )

                    OutlinedTextField(
                        value = formattedUserBirthdate,
                        onValueChange = {},
                        label = { Text("Birth Date") },
                        readOnly = true,
                        modifier = textFieldModifier,
                        shape = RoundedCornerShape(16.dp),
                        colors = fieldColors.copy(
                            disabledLabelColor = Color(0xFF8C5E4D),
                            disabledTextColor = Color(0xFF222222)
                        )
                    )

                    Button(
                        onClick = { showDatePicker.value = true },
                        modifier = Modifier
                            .padding(vertical = 6.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDD7F12)),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Select Birth Date", color = Color.White)
                    }

                    if (showDatePicker.value) {
                        val datePickerState = rememberDatePickerState(
                            userBirthdate.value ?: System.currentTimeMillis()
                        )
                        DatePickerDialog(
                            onDismissRequest = { showDatePicker.value = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    userBirthdate.value = datePickerState.selectedDateMillis
                                    showDatePicker.value = false
                                }) {
                                    Text("Accept", color = Color(0xFF572365))
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDatePicker.value = false }) {
                                    Text("Cancel", color = Color(0xFF8C5E4D))
                                }
                            }
                        ) {
                            DatePicker(state = datePickerState)
                        }
                    }
                }

                item {
                    Text(
                        "Gender:",
                        color = Color(0xFF222222),
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    Row(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        genders.forEach { gender ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { userGender = gender._id }
                            ) {
                                RadioButton(
                                    selected = userGender == gender._id,
                                    onClick = { userGender = gender._id },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = Color(
                                            0xFF572365
                                        )
                                    )
                                )
                                Text(text = gender.name, modifier = Modifier.padding(start = 4.dp))
                            }
                        }
                    }
                }

                item {
                    val fieldColors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF572365),
                        unfocusedBorderColor = Color(0xFF8C5E4D),
                        focusedLabelColor = Color(0xFF572365),
                        unfocusedLabelColor = Color(0xFF8C5E4D),
                        cursorColor = Color(0xFF572365),
                        disabledTextColor = Color(0xFF222222)
                    )

                    listOf(
                        Triple(userAddress, "Address", 500),
                        Triple(userEmail, "Email", 50),
                        Triple(userPassword, "Password", 50),
                        Triple(userBiography, "Biography", 300)
                    ).forEach { (field, label, _) ->
                        OutlinedTextField(
                            value = field.value,
                            onValueChange = { field.value = it },
                            label = { Text(label) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = fieldColors
                        )
                    }
                }

                item {
                    Button(
                        onClick = { photoPickerLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8C5E4D)),
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Select Profile Image", color = Color.White)
                    }

                    imageUri?.let {
                        Image(
                            painter = rememberAsyncImagePainter(it),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .padding(8.dp)
                        )
                    }
                }

                item {
                    Button(
                        onClick = {
                            // VALIDACIÓN ORIGINAL MANTENIDA...
                            val emptyFields = listOfNotNull(
                                if (userName.value.isBlank()) "Name" else null,
                                if (userLastname.value.isBlank()) "Last Name" else null,
                                if (userBirthdate.value == null) "Birth Date" else null,
                                if (userGender == null) "Gender" else null,
                                if (userAddress.value.isBlank()) "Address" else null,
                                if (userEmail.value.isBlank()) "Email" else null,
                                if (userPassword.value.isBlank()) "Password" else null
                            )

                            if (emptyFields.isNotEmpty()) {
                                Toast.makeText(
                                    context,
                                    "${emptyFields.joinToString(", ")} required",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            val tooLongFields = listOfNotNull(
                                if (userName.value.length > 30) "Name (${userName.value.length}/30)" else null,
                                if (userLastname.value.length > 30) "Last Name (${userLastname.value.length}/30)" else null,
                                if (userAddress.value.length > 500) "Address (${userAddress.value.length}/500)" else null,
                                if (userEmail.value.length > 50) "Email (${userEmail.value.length}/50)" else null,
                                if (userBiography.value.length > 300) "Biography (${userBiography.value.length}/300)" else null
                            )

                            if (tooLongFields.isNotEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Too long: ${tooLongFields.joinToString()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            if (userPassword.value.length < 6) {
                                Toast.makeText(
                                    context,
                                    "Password must be at least 6 characters",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            if (userAge < 16) {
                                Toast.makeText(
                                    context,
                                    "You must be at least 16 years old",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            if (!isValidEmail(userEmail.value)) {
                                Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT)
                                    .show()
                                return@Button
                            }

                            val user = RegisterInfo(
                                name = userName.value,
                                last_name = userLastname.value,
                                email = userEmail.value,
                                biography = userBiography.value,
                                gender = userGender.orEmpty(),
                                birthdate = formattedUserBirthdate,
                                user_img = imageUri.toString(),
                                address = userAddress.value,
                                password = userPassword.value
                            )

                            authViewModel.register(user)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF572365)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Register", color = Color.White)
                    }
                }
            }
        }
    }
}
