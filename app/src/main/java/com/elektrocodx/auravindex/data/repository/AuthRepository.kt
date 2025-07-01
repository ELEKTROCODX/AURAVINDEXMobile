package com.elektrocodx.auravindex.data.repository

import com.elektrocodx.auravindex.retrofit.AuthClient
import com.elektrocodx.auravindex.retrofit.LoginRequest
import com.elektrocodx.auravindex.retrofit.RegisterInfo

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