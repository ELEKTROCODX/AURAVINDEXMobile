package com.elektrocodx.auravindex.mapper

import com.elektrocodx.auravindex.model.User
import com.elektrocodx.auravindex.model.local.relations.UserWithRelations


fun UserWithRelations.toDomain(): User {
    return User(
        __v = user.__v,
        _id = user._id,
        createdAt = user.createdAt,
        updatedAt = user.updatedAt,
        name = user.name,
        last_name = user.last_name,
        email = user.email,
        gender = gender.toDomain(),
        role = role.toDomain(),
        address = user.address,
        biography = user.biography,
        birthdate = user.birthdate,
        user_img = user.user_img
    )
}