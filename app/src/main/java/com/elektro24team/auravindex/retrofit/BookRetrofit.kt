package com.elektro24team.auravindex.retrofit

import com.elektro24team.auravindex.model.ApiResponse
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService{
    @GET("book")
    suspend fun getBooks(
        @Query("show_duplicates") showDuplicates: Boolean = true,
        @Query("show_lents") showLents: Boolean = true,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): ApiResponse<List<Book>>

    @GET("book/latest_releases")
    suspend fun getLatestReleases(
        @Query("limit") limit: Int = 10,
    ): ApiResponse<List<Book>>
}

object BookClient{
    val apiService: BookService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookService::class.java)
    }
}