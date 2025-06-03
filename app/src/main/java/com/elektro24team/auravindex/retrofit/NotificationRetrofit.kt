package com.elektro24team.auravindex.retrofit

import com.elektro24team.auravindex.model.ActivePlan
import com.elektro24team.auravindex.model.ApiResponse
import com.elektro24team.auravindex.model.Notification
import com.elektro24team.auravindex.model.api.NotificationRequest
import com.elektro24team.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationService {
    @GET("notification")
    suspend fun getNotifications(
        @Header("Authorization") token: String,
        @Query("page") page: String = "1",
        @Query("limit") limit: String = "none"
    ): ApiResponse<List<Notification>>
    @GET("notification")
    suspend fun getUserNotifications(
        @Header("Authorization") token: String,
        @Query("filter_field") filterField: String? = "receiver",
        @Query("filter_value") filterValue: String,
        @Query("page") page: String = "1",
        @Query("limit") limit: String = "none"
    ): ApiResponse<List<Notification>>
    @GET("notification/{id}")
    suspend fun getNotificationById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Notification
    @POST("notification")
    suspend fun createNotification(
        @Header("Authorization") token: String,
        @Body notification: NotificationRequest
    )
    @PUT("notification/{id}/mark_as_read")
    suspend fun markNotificationAsRead(
        @Header("Authorization") token: String,
        @Path("id") id: String
    )
}

object NotificationClient{
    val apiService: NotificationService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotificationService::class.java)
    }
}