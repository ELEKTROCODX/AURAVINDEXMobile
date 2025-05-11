package com.elektro24team.auravindex.mapper

import com.elektro24team.auravindex.model.BookCollection
import com.elektro24team.auravindex.model.local.BookCollectionEntity

fun BookCollection.toEntity(): BookCollectionEntity {
    return BookCollectionEntity(
        _id = this._id,
        name = this.name,
        __v = this.__v,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
fun BookCollectionEntity.toDomain(): BookCollection {
    return BookCollection(
        _id = this._id,
        name = this.name,
        __v = this.__v,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
