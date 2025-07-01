package com.elektrocodx.auravindex.retrofit

import com.elektrocodx.auravindex.model.ApiResponse
import com.elektrocodx.auravindex.model.TokenData
import com.elektrocodx.auravindex.utils.constants.URLs.BASE_URL
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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
    val address: String,
    val password: String
)
interface AuthService{
    @POST("auth/login/")
    suspend fun loginUser(@Body credentials: LoginRequest): TokenData
    @Multipart
    @POST("auth/register/")
    suspend fun registerUser(
        @Part("name") name: RequestBody,
        @Part("last_name") lastName: RequestBody,
        @Part("email") email: RequestBody,
        @Part("biography") biography: RequestBody,
        @Part("gender") gender: RequestBody, // Assuming gender is sent as its ID string
        @Part("birthdate") birthdate: RequestBody, // Formatted date string
        @Part("address") address: RequestBody,
        @Part("password") password: RequestBody,
        @Part user_img: MultipartBody.Part?,
    ): ApiResponse<String>
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
