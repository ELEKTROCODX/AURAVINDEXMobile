package com.elektrocodx.auravindex.retrofit

import com.elektrocodx.auravindex.model.ApiResponse
import com.elektrocodx.auravindex.model.User
import com.elektrocodx.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService{
    @GET("user")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Query("filter_field") filterField: String,
        @Query("filter_value") filterValue: String,
        @Query("sort") sort: String? = "asc",
        @Query("sort_by") sortBy: String? = "createdAt"
    ): ApiResponse<List<User>>
    @GET("user")
    suspend fun getUsers(
        @Header("Authorization") token: String,
        @Query("page") page: String = "1",
        @Query("limit") limit: String = "none"
    ): ApiResponse<List<User>>
    @GET("user/{id}")
    suspend fun getUserById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): User
    @POST("user/{id}/fcm_token")
    suspend fun updateFcmToken(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body fcmToken: Map<String, String>
    ): Response<Void>
}
object UserClient{
    val apiService: UserService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserService::class.java)
    }
}