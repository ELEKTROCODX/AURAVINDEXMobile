package com.elektrocodx.auravindex.mapper

import com.elektrocodx.auravindex.model.Author
import com.elektrocodx.auravindex.model.Book
import com.elektrocodx.auravindex.model.BookCollection
import com.elektrocodx.auravindex.model.BookStatus
import com.elektrocodx.auravindex.model.Editorial
import com.elektrocodx.auravindex.model.local.BookEntity

fun Book.toEntity(): BookEntity {
    return BookEntity(
        __v = this.__v,
        _id = this._id,
        book_collection_id = this.book_collection._id,
        book_img = this.book_img,
        book_status_id = this.book_status._id,
        classification = this.classification,
        createdAt = this.createdAt,
        edition = this.edition,
        editorial_id = this.editorial._id,
        authors_id = this.authors.map { it._id },
        genres = this.genres,
        isbn = this.isbn,
        language = this.language,
        location = this.location,
        sample = this.sample,
        summary = this.summary,
        title = this.title,
        updatedAt = this.updatedAt
    )
}

fun BookEntity.toDomain(
    authors: List<Author>,
    bookCollection: BookCollection,
    bookStatus: BookStatus,
    editorial: Editorial
): Book {
    return Book(
        __v = this.__v,
        _id = this._id,
        authors = authors,
        book_collection = bookCollection,
        book_img = this.book_img,
        book_status = bookStatus,
        classification = this.classification,
        createdAt = this.createdAt,
        edition = this.edition,
        editorial = editorial,
        genres = this.genres,
        isbn = this.isbn,
        language = this.language,
        location = this.location,
        sample = this.sample,
        summary = this.summary,
        title = this.title,
        updatedAt = this.updatedAt
    )
}