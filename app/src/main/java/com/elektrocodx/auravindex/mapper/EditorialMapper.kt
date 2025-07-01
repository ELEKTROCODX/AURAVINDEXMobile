package com.elektrocodx.auravindex.mapper

import com.elektrocodx.auravindex.model.Editorial
import com.elektrocodx.auravindex.model.local.EditorialEntity

fun Editorial.toEntity(): EditorialEntity {
    return EditorialEntity(
        _id = this._id,
        name = this.name,
        address = this.address,
        email = this.email,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        __v = this.__v
    )
}

fun EditorialEntity.toDomain(): Editorial {
    return Editorial(
        _id = this._id,
        name = this.name,
        address = this.address,
        email = this.email,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        __v = this.__v
    )
}