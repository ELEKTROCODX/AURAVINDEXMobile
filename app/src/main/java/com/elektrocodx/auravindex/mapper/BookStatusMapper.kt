package com.elektrocodx.auravindex.mapper

import com.elektrocodx.auravindex.model.BookStatus
import com.elektrocodx.auravindex.model.local.BookStatusEntity

fun BookStatus.toEntity(): BookStatusEntity {
    return BookStatusEntity(
        __v = this.__v,
        _id = this._id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        book_status = this.book_status
    )
}

fun BookStatusEntity.toDomain(): BookStatus {
    return BookStatus(
        __v = this.__v,
        _id = this._id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        book_status = book_status
    )
}