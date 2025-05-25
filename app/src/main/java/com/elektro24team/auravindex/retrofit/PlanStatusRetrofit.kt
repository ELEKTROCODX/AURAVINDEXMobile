package com.elektro24team.auravindex.retrofit

import com.elektro24team.auravindex.model.ApiResponse
import com.elektro24team.auravindex.model.PlanStatus
import com.elektro24team.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface PlanStatusService{
    @GET("plan_status")
    suspend fun getPlanStatuses(
        @Query("page") page: String = "1",
        @Query("limit") limit: String = "none"
    ): ApiResponse<List<PlanStatus>>

    @GET("plan_status/{id}")
    suspend fun getPlanStatusById(
        @Path("id") id: String
    ): PlanStatus

    @GET("plan_status")
    suspend fun getPlanStatusByName(
        @Query("filter_field") filterField: String = "plan_status",
        @Query("filter_value") filterValue: String,
    ): ApiResponse<List<PlanStatus>>
}

object PlanStatusClient{
    val apiService: PlanStatusService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlanStatusService::class.java)
    }
}