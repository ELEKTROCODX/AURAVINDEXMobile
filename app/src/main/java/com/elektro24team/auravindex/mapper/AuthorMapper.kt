package com.elektro24team.auravindex.mapper

import com.elektro24team.auravindex.model.Author
import com.elektro24team.auravindex.model.local.AuthorEntity

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