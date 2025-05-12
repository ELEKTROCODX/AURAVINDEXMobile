package com.elektro24team.auravindex.data.repository

import android.util.Log
import com.elektro24team.auravindex.retrofit.AuthClient
import com.elektro24team.auravindex.retrofit.LoginRequest

class AuthRepository {
    suspend fun login(email: String, password: String): Result<String>{
        return try{
            val response = AuthClient.apiService.loginUser(LoginRequest(email, password))
            //Log.d("LOGIN_RESPONSE", response.toString())
            val token = response.token
            Result.success(token)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}