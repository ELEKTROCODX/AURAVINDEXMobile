package com.elektro24team.auravindex.model

data class RecentBook(
    val __v: Int,
    val _id: String,
    val books: List<Book>,
    val createdAt: String,
    val updatedAt: String,
    val user: User
)