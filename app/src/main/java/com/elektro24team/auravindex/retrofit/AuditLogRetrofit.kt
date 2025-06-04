package com.elektro24team.auravindex.retrofit

import com.elektro24team.auravindex.model.ApiResponse
import com.elektro24team.auravindex.model.AuditLog
import com.elektro24team.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface AuditLogService{
    @GET("audit_log")
    suspend fun getAuditLogs(
        @Header("Authorization") token: String,
        @Query("page") page: String = "1",
        @Query("limit") limit: String = "none",
        @Query("sort") sort: String? = "asc",
        @Query("sort_by") sortBy: String? = "createdAt"
    ): ApiResponse<List<AuditLog>>

    @GET("audit_log/{id}")
    suspend fun getAuditLogById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): AuditLog
}
object AuditLogClient{
    val apiService: AuditLogService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuditLogService::class.java)
    }
}