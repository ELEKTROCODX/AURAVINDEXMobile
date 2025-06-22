package com.elektro24team.auravindex.model.api

data class BookListRequest (
    val owner: String,
    val title: String,
    val description: String,
    val books: List<String>
)