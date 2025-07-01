package com.elektrocodx.auravindex.data.repository

import com.elektrocodx.auravindex.data.local.dao.BookCollectionDao
import com.elektrocodx.auravindex.model.BookCollection
import com.elektrocodx.auravindex.model.local.BookCollectionEntity
import com.elektrocodx.auravindex.retrofit.BookCollectionClient

class BookCollectionRepository(
    private val bookCollectionDao: BookCollectionDao
) {
    private val CACHE_EXPIRY_MS = 24 * 60 * 60 * 1000L // 1 día
    @Volatile
    private var lastCacheTime: Long = 0

    suspend fun getBookCollections(): List<BookCollectionEntity> {
        val currentTime = System.currentTimeMillis()
        val isCacheExpired = (currentTime - lastCacheTime) > CACHE_EXPIRY_MS

        return if (bookCollectionDao.getAllBookCollections().isEmpty() || isCacheExpired) {
            try {
                val response = BookCollectionClient.apiService.getBookCollections()
                val bookCollectionEntities = response.data.map { it.toEntity() }
                bookCollectionDao.clearBookCollections()
                bookCollectionDao.insertBookCollections(bookCollectionEntities)
                lastCacheTime = currentTime
                bookCollectionEntities
            } catch (e: Exception) {
                // Fall back to local data if API fails
                bookCollectionDao.getAllBookCollections()
            }
        } else {
            bookCollectionDao.getAllBookCollections()
        }
    }

    private fun BookCollection.toEntity(): BookCollectionEntity {
        return BookCollectionEntity(
            _id = this._id,
            name = this.name,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            __v = this.__v
        )
    }
}