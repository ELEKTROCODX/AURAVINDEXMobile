package com.elektrocodx.auravindex.retrofit

import com.elektrocodx.auravindex.model.ApiResponse
import com.elektrocodx.auravindex.model.BookCollection
import com.elektrocodx.auravindex.utils.constants.URLs.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BookCollectionService{
    @GET("book_collection")
    suspend fun getBookCollections(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 15
    ): ApiResponse<List<BookCollection>>
}
object BookCollectionClient{
    val apiService: BookCollectionService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookCollectionService::class.java)
    }
}