package com.elektrocodx.auravindex.mapper

import com.elektrocodx.auravindex.model.Author
import com.elektrocodx.auravindex.model.local.AuthorEntity

fun Author.toEntity(): AuthorEntity {
    return AuthorEntity(
        __v = this.__v,
        _id = this._id,
        birthdate = this.birthdate,
        createdAt = this.createdAt,
        gender = this.gender,
        last_name = this.last_name,
        name = this.name,
        updatedAt = this.updatedAt
    )
}
fun AuthorEntity.toDomain(): Author {
    return Author(
        __v = this.__v,
        _id = this._id,
        birthdate = this.birthdate,
        createdAt = this.createdAt,
        gender = this.gender,
        last_name = this.last_name,
        name = this.name,
        updatedAt = this.updatedAt
    )
}