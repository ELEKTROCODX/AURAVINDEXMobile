package com.elektro24team.auravindex.retrofit

import com.elektro24team.auravindex.model.TokenData
import com.elektro24team.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
//Model of the login request
data class LoginRequest(val email: String, val password: String)

interface AuthService{
    @POST("auth/login/")
    suspend fun loginUser(@Body credentials: LoginRequest): TokenData
}

object AuthClient{
    val apiService: AuthService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthService::class.java)
    }
}
