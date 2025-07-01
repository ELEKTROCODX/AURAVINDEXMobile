package com.elektrocodx.auravindex.retrofit

import com.elektrocodx.auravindex.model.ApiResponse
import com.elektrocodx.auravindex.model.Plan
import com.elektrocodx.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface PlanService{
    @GET("plan")
    suspend fun getPlans(): ApiResponse<List<Plan>>
    @GET("plan/{planId}")
    suspend fun getPlanById(planId: String): Plan
}
object PlanClient{
    val apiService: PlanService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlanService::class.java)
    }
}