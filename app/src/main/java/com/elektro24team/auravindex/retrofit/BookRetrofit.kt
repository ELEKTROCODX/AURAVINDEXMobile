package com.elektro24team.auravindex.retrofit

import com.elektro24team.auravindex.model.ApiResponse
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface BookService{
    @GET("book")
    suspend fun getBooks(
        @Query("show_duplicates") showDuplicates: Boolean = true,
        @Query("show_lents") showLents: Boolean = true,
        @Query("page") page: String = "1",
        @Query("limit") limit: String = "none"
    ): ApiResponse<List<Book>>
    @GET("book/latest_releases")
    suspend fun getLatestReleases(
        @Query("limit") limit: String = "10",
    ): ApiResponse<List<Book>>
    @GET("book/{id}")
    suspend fun getBookById(
        @Path("id") id: String
    ): Book
    @GET("book/{id}")
    suspend fun getBookByIdWithAuth(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Book
    @GET("book")
    suspend fun getFilteredBooks(
        @Query("show_duplicates") showDuplicates: Boolean = true,
        @Query("show_lents") showLents: Boolean = true,
        @Query("filter_field") filterField: String,
        @Query("filter_value") filterValue: String,
        @Query("page") page: String = "1",
        @Query("limit") limit: String = "none"
    ):ApiResponse<List<Book>>
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