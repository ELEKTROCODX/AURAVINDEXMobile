package com.elektro24team.auravindex.model

data class BookList (
    val __v: Int,
    val _id: String,
    val title: String,
    val description: String,
    val owner: User,
    val books: List<Book>,
    val createdAt: String,
    val updatedAt: String,
)