package com.elektro24team.auravindex.retrofit

import com.elektro24team.auravindex.model.ApiResponse
import com.elektro24team.auravindex.model.ActivePlan
import com.elektro24team.auravindex.model.api.ActivePlanRequest
import com.elektro24team.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface ActivePlanService{
    @GET("active_plan")
    suspend fun getActivePlans(
        @Header("Authorization") token: String,
        @Query("page") page: String = "1",
        @Query("limit") limit: String = "none"
    ): ApiResponse<List<ActivePlan>>

    @GET("active_plan")
    suspend fun getActivePlanByUserId(
        @Header("Authorization") token: String,
        @Query("filter_field") filterField: String? = "user",
        @Query("filter_value") filterValue: String
    ): ApiResponse<List<ActivePlan>>

    @GET("active_plan/{id}")
    suspend fun getActivePlanById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): ActivePlan

    @POST("active_plan")
    suspend fun createActivePlan(
        @Header("Authorization") token: String,
        @Body activePlan: ActivePlanRequest
    )
    @PUT("active_plan/{id}/renewal")
    suspend fun renewActivePlan(
        @Header("Authorization") token: String,
        @Path("id") id: String
    )
    @PUT("active_plan/{id}/finish")
    suspend fun finishActivePlan(
        @Header("Authorization") token: String,
        @Path("id") id: String
    )
    @PUT("active_plan/{id}/cancel")
    suspend fun cancelActivePlan(
        @Header("Authorization") token: String,
        @Path("id") id: String
    )
}

object ActivePlanClient{
    val apiService: ActivePlanService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ActivePlanService::class.java)
    }
}