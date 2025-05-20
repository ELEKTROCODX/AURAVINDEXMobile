package com.elektro24team.auravindex.retrofit

import com.elektro24team.auravindex.model.ApiResponse
import com.elektro24team.auravindex.model.AuditLog
import com.elektro24team.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

//Interface with API calls
interface AuditLogService{
    @GET("audit_log")
    suspend fun getAuditLogs(
        @Header("Authorization") token: String
    ): ApiResponse<List<AuditLog>>

    @GET("audit_log/{id}")
    suspend fun getAuditLogById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): AuditLog
}
//Object that manage the AuditLogService, is a singleton instance
object AuditLogClient{
    val apiService: AuditLogService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuditLogService::class.java)
    }
}