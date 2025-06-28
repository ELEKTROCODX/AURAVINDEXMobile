package com.elektro24team.auravindex.mapper

import com.elektro24team.auravindex.model.Gender
import com.elektro24team.auravindex.model.local.GenderEntity

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