package com.elektro24team.auravindex.model

data class User(
    val __v: Int,
    val _id: String,
    val address: String?,
    val biography: String,
    val birthdate: String,
    val createdAt: String,
    val email: String,
    val gender: Gender,
    val last_name: String,
    val name: String,
    val role: Role,
    val updatedAt: String,
    val user_img: String
)