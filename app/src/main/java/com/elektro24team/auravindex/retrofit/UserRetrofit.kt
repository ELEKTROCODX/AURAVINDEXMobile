package com.elektro24team.auravindex.retrofit

import com.elektro24team.auravindex.model.ApiResponse
import com.elektro24team.auravindex.model.User
import com.elektro24team.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService{
    @GET("user")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Query("filter_field") filterField: String,
        @Query("filter_value") filterValue: String,
    ): ApiResponse<List<User>>

    @GET("user")
    suspend fun getUsers(
        @Header("Authorization") token: String,
    ): ApiResponse<List<User>>

    @GET("user/{id}")
    suspend fun getUserById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): User

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