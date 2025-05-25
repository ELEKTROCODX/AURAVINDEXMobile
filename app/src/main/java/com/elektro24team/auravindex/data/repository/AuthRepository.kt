package com.elektro24team.auravindex.data.repository

import android.util.Log
import com.elektro24team.auravindex.retrofit.AuthClient
import com.elektro24team.auravindex.retrofit.LoginRequest
import com.elektro24team.auravindex.retrofit.RegisterInfo

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
    suspend fun register(userData: RegisterInfo): Result<String>{
        return try{
            val response = AuthClient.apiService.registerUser(userData)
            Result.success(response.data)
        }catch (e: Exception){
            Result.failure(e)
        }
    }
}