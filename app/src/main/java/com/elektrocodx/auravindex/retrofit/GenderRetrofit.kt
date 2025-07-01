package com.elektrocodx.auravindex.retrofit

import com.elektrocodx.auravindex.model.ApiResponse
import com.elektrocodx.auravindex.model.Gender
import com.elektrocodx.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface GenderService{
    @GET("gender")
    suspend fun getGenders(): ApiResponse<List<Gender>>
}
object GenderClient{
    val apiService : GenderService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GenderService::class.java)
    }
}