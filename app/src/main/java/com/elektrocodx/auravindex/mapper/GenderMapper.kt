package com.elektrocodx.auravindex.mapper

import com.elektrocodx.auravindex.model.Gender
import com.elektrocodx.auravindex.model.local.GenderEntity

fun Gender.toEntity(): GenderEntity {
    return GenderEntity(
        __v = this.__v,
        _id = this._id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        name = this.name,
    )
}

fun GenderEntity.toDomain(): Gender {
    return Gender(
        __v = this.__v,
        _id = this._id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        name = this.name,
    )
}