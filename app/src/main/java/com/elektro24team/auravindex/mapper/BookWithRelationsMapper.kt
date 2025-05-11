package com.elektro24team.auravindex.mapper

import com.elektro24team.auravindex.model.Book
import com.elektro24team.auravindex.model.local.relations.BookWithRelations

fun BookWithRelations.toDomain(): Book {
    return Book(
        __v = book.__v,
        _id = book._id,
        authors = authors.map { it.toDomain() },
        book_collection = bookCollection.toDomain(),
        book_img = book.book_img,
        book_status = bookStatus.toDomain(),
        classification = book.classification,
        createdAt = book.createdAt,
        edition = book.edition,
        editorial = editorial.toDomain(),
        genres = book.genres,
        isbn = book.isbn,
        language = book.language,
        location = book.location,
        sample = book.sample,
        summary = book.summary,
        title = book.title,
        updatedAt = book.updatedAt
    )
}