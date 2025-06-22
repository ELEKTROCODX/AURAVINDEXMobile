package com.elektro24team.auravindex.retrofit

import com.elektro24team.auravindex.model.ApiResponse
import com.elektro24team.auravindex.model.TokenData
import com.elektro24team.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
data class LoginRequest(
    val email: String,
    val password: String,
    val expires_in: String? = "4w"
)

data class RegisterInfo(
    val name: String,
    val last_name: String,
    val email: String,
    val biography: String,
    val gender: String,
    val birthdate: String,
    val user_img: String,
    val address: String,
    val password: String
)
interface AuthService{
    @POST("auth/login/")
    suspend fun loginUser(@Body credentials: LoginRequest): TokenData
    @POST("auth/register/")
    suspend fun registerUser(@Body userData: RegisterInfo): ApiResponse<String>
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
