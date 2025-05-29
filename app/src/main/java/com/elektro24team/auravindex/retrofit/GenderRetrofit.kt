package com.elektro24team.auravindex.retrofit

import com.elektro24team.auravindex.model.ApiResponse
import com.elektro24team.auravindex.model.Gender
import retrofit2.Retrofit
import retrofit2.http.GET
import com.elektro24team.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.converter.gson.GsonConverterFactory

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