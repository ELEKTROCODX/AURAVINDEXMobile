package com.elektro24team.auravindex.mapper

import com.elektro24team.auravindex.model.Role
import com.elektro24team.auravindex.model.local.RoleEntity

fun Role.toEntity(): RoleEntity {
    return RoleEntity(
        __v = this.__v,
        _id = this._id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        name = this.name,
        permissions = this.permissions
    )
}

fun RoleEntity.toDomain(): Role {
    return Role(
        __v = this.__v,
        _id = this._id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        name = this.name,
        permissions = this.permissions

    )
}