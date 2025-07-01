package com.elektrocodx.auravindex.model

data class Role(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val name: String,
    val permissions: List<String>,
    val updatedAt: String
)