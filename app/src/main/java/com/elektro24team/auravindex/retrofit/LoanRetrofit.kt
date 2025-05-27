package com.elektro24team.auravindex.retrofit

import com.elektro24team.auravindex.model.ApiResponse
import com.elektro24team.auravindex.model.Loan
import com.elektro24team.auravindex.model.api.LoanRequest
import com.elektro24team.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface LoanService{
    @GET("loan")
    suspend fun getLoans(
        @Header("Authorization") token: String,
        @Query("page") page: String = "1",
        @Query("limit") limit: String = "none"
    ): ApiResponse<List<Loan>>

    @GET("loan")
    suspend fun getUserLoans(
        @Header("Authorization") token: String,
        @Query("page") page: String = "1",
        @Query("limit") limit: String = "none",
        @Query("filter_field") filterField: String = "user",
        @Query("filter_value") filterValue: String
    ): ApiResponse<List<Loan>>

    @GET("loan/{id}")
    suspend fun getLoanById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Loan

    @POST("loan")
    suspend fun createLoan(
        @Header("Authorization") token: String,
        @Body loan: LoanRequest
    )
}

object LoanClient{
    val apiService: LoanService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoanService::class.java)
    }
}