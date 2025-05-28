package com.elektro24team.auravindex.model.api

data class BookListRequest (
    val user: String,
    val title: String,
    val description: String,
    val books: List<String>

)