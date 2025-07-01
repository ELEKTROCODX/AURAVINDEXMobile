package com.elektrocodx.auravindex.retrofit

import com.elektrocodx.auravindex.model.ApiResponse
import com.elektrocodx.auravindex.model.RecentBook
import com.elektrocodx.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface RecentBookService{
    @GET("recent_book/{id}")
    suspend fun getRecentBooksById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): RecentBook
    @GET("recent_book/")
    suspend fun getRecentBooksByUserId(
        @Header("Authorization") token: String,
        @Query("filter_field") filterField: String = "user",
        @Query("filter_value") filterValue: String,
    ): ApiResponse<List<RecentBook>>
}
object RecentBookClient{
    val apiService: RecentBookService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecentBookService::class.java)
    }
}