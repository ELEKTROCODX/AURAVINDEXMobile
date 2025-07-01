package com.elektro24team.auravindex.mapper

import com.elektro24team.auravindex.model.Gender
import com.elektro24team.auravindex.model.Role
import com.elektro24team.auravindex.model.User
import com.elektro24team.auravindex.model.local.UserEntity


fun User.toEntity(): UserEntity {
    return UserEntity(
        __v = this.__v,
        _id = this._id,
        name = this.name,
        last_name = this.last_name,
        email = this.email,
        birthdate = this.birthdate,
        address = this.address,
        biography = this.biography,
        gender_id = this.gender._id,
        role_id = this.role._id,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        user_img = this.user_img
    )
}

fun UserEntity.toDomain(
    gender: Gender,
    role: Role
): User {
    return User(
        __v = this.__v,
        _id = this._id,
        name = this.name,
        last_name = this.last_name,
        email = this.email,
        birthdate = this.birthdate,
        address = this.address,
        biography = this.biography,
        gender = gender,
        role = role,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        user_img = this.user_img

    )
}