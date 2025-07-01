package com.elektrocodx.auravindex.data.repository

import android.content.Context
import android.net.Uri
import com.elektrocodx.auravindex.retrofit.AuthClient
import com.elektrocodx.auravindex.retrofit.LoginRequest
import com.elektrocodx.auravindex.retrofit.RegisterInfo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class AuthRepository {
    suspend fun login(email: String, password: String): Result<String>{
        return try{
            val response = AuthClient.apiService.loginUser(LoginRequest(email, password))
            val token = response.token
            Result.success(token)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
    suspend fun register(userData: RegisterInfo, imageUri: Uri?, context: Context): Result<String>{
        return try {
            val namePart = userData.name.toRequestBody("text/plain".toMediaTypeOrNull())
            val lastNamePart = userData.last_name.toRequestBody("text/plain".toMediaTypeOrNull())
            val emailPart = userData.email.toRequestBody("text/plain".toMediaTypeOrNull())
            val biographyPart = userData.biography.toRequestBody("text/plain".toMediaTypeOrNull())
            val genderPart = userData.gender.toRequestBody("text/plain".toMediaTypeOrNull())
            val birthdatePart = userData.birthdate.toRequestBody("text/plain".toMediaTypeOrNull())
            val addressPart = userData.address.toRequestBody("text/plain".toMediaTypeOrNull())
            val passwordPart = userData.password.toRequestBody("text/plain".toMediaTypeOrNull())

            var imageMultipart: MultipartBody.Part? = null
            imageUri?.let { uri ->
                val inputStream = context.contentResolver.openInputStream(uri)
                inputStream?.use { input ->
                    val imageBytes = input.readBytes()

                    val requestBody = imageBytes.toRequestBody("image/*".toMediaTypeOrNull(), 0, imageBytes.size)
                    imageMultipart = MultipartBody.Part.createFormData("user_img", "profile.jpg", requestBody)
                }
            }

            val response = AuthClient.apiService.registerUser(
                name = namePart,
                lastName = lastNamePart,
                email = emailPart,
                biography = biographyPart,
                gender = genderPart,
                birthdate = birthdatePart,
                address = addressPart,
                password = passwordPart,
                user_img = imageMultipart
            )

            Result.success(response.data)

        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}