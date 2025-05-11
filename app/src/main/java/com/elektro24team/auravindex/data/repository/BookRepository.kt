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

    suspend fun getBookById(bookId: String): Book {
        val local = bookDao.getBookWithRelations(bookId)
        if (local != null) {
            return local.toDomain()
        }

        val remote = BookClient.apiService.getBookById(bookId)
        saveBookToCache(remote)

        return remote
    }

    suspend fun getAllBooks(): List<Book> {
        val local = bookDao.getAllBooksWithRelations()
        if (local.isNotEmpty()) {
            return local.map { it.toDomain() }
        } else {
            val remote = BookClient.apiService.getBooks(limit = "none")
            return remote.data.map { it }
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