package com.elektro24team.auravindex.retrofit

import com.elektro24team.auravindex.model.ApiResponse
import com.elektro24team.auravindex.model.BookList
import com.elektro24team.auravindex.model.api.BookListRequest
import com.elektro24team.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BookListService{
    @GET("book_list")
    suspend fun getBookLists(
        @Query("show_duplicates") showDuplicates: Boolean = true,
        @Query("show_lents") showLents: Boolean = true,
        @Query("page") page: String = "1",
        @Query("limit") limit: String = "none"
    ): ApiResponse<List<BookList>>

    @GET("book_list/{id}")
    suspend fun getBookListById(
        @Path("id") id: String
    ): BookList

    @GET("book_list")
    suspend fun getUserBookLists(
        @Header("Authorization") token: String,
        @Query("filter_field") filterField: String = "owner",
        @Query("filter_value") filterValue: String,
        @Query("page") page: String = "1",
        @Query("limit") limit: String = "none"
    ):ApiResponse<List<BookList>>

    @POST("book_list")
    suspend fun createBookList(
        @Header("Authorization") token: String,
        @Body bookList: BookListRequest
    )

    @POST("book_list/{bookListId}/book/{bookId}")
    suspend fun addBookToList(
        @Header("Authorization") token: String,
        @Path("bookListId") bookListId: String,
        @Path("bookId") bookId: String
    )

    @DELETE("book_list/{bokListId}/book/{bookId}")
    suspend fun removeBookFromList(
        @Header("Authorization") token: String,
        @Path("bokListId") bookListId: String,
        @Path("bookId") bookId: String
    )

    @DELETE("book_list/{bookListId}")
    suspend fun deleteList(
        @Header("Authorization") token: String,
        @Path("bookListId") bookListId: String
    )
}

object BookListClient{
    val apiService: BookListService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookListService::class.java)
    }
}