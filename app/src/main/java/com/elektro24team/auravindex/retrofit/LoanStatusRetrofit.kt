package com.elektro24team.auravindex.retrofit

import com.elektro24team.auravindex.model.ApiResponse
import com.elektro24team.auravindex.model.LoanStatus
import com.elektro24team.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface LoanStatusService{
    @GET("loan_status")
    suspend fun getLoanStatuses(
        @Query("page") page: String = "1",
        @Query("limit") limit: String = "none"
    ): ApiResponse<List<LoanStatus>>

    @GET("loan_status/{id}")
    suspend fun getLoanStatusById(
        @Path("id") id: String
    ): LoanStatus

    @GET("loan_status")
    suspend fun getLoanStatusByName(
        @Query("filter_field") filterField: String = "loan_status",
        @Query("filter_value") filterValue: String,
    ): ApiResponse<List<LoanStatus>>
}

object LoanStatusClient{
    val apiService: LoanStatusService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoanStatusService::class.java)
    }
}