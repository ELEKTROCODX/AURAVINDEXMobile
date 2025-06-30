package com.elektro24team.auravindex.data.repository

import com.elektro24team.auravindex.model.local.BookAuthorCrossRef
import com.elektro24team.auravindex.data.local.dao.BookDao
import com.elektro24team.auravindex.mapper.toDomain
import com.elektro24team.auravindex.mapper.toEntity
import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.retrofit.BookClient


class BookRepository(
    private val bookDao: BookDao
) {
    private val CACHE_EXPIRY_MS = 10 * 60 * 1000L // 10 min
    @Volatile
    private var lastCacheTime: Long = 0

    suspend fun getAllBooks(
        showDuplicates: Boolean,
        showLents: Boolean
    ): List<Book> {
        val currentTime = System.currentTimeMillis()
        val isCacheExpired = (currentTime - lastCacheTime) > CACHE_EXPIRY_MS
        val local = bookDao.getAllBooksWithRelations()
        return if (local.isEmpty() || isCacheExpired) {
            val remote = BookClient.apiService.getBooks(
                showDuplicates,
                showLents
            )
            remote.data.map { it }
        } else {
            local.map { it.toDomain() }
        }
    }
    suspend fun getBookById(bookId: String): Book {
        val local = bookDao.getBookWithRelations(bookId)
        if (local != null) {
            return local.toDomain()
        }

        val remote = BookClient.apiService.getBookById(bookId)
        saveBookToCache(remote)
        return remote
    }
    suspend fun getBookByIdWithAuth(token: String, bookId: String): Result<Book> {
        return try {
            val remote = BookClient.apiService.getBookByIdWithAuth("Bearer $token", bookId)
            saveBookToCache(remote)
            Result.success(remote)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun saveBookToCache(book: Book) {
        val bookEntity = book.toEntity()
        val editorial = book.editorial.toEntity()
        val status = book.book_status.toEntity()
        val collection = book.book_collection.toEntity()
        val authors = book.authors.map { it.toEntity() }
        val crossRefs = book.authors.map { BookAuthorCrossRef(book._id, it._id) }

        bookDao.insertBookWithRelations(
            bookEntity,
            authors,
            editorial,
            status,
            collection,
            crossRefs
        )
    }
}