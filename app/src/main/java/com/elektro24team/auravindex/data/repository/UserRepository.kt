package com.elektro24team.auravindex.data.repository

import android.util.Log
import com.elektro24team.auravindex.model.User
import com.elektro24team.auravindex.retrofit.UserClient

class UserRepository {
    suspend fun getUser(token: String, email: String): Result<User>{
        return try {
            val response = UserClient.apiService.getUser(token = token,filterField = "email",filterValue = email)
            Log.d("UserRepository", "Usuario recibido: ${response}")
            val user = response.data?.firstOrNull()
            user?.let {
                Log.d("UserRepository", "Usuario encontrado: ${it.name}")
                Result.success(it)
            } ?: run {
                Result.failure(Exception("No se encontró un usuario"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}

