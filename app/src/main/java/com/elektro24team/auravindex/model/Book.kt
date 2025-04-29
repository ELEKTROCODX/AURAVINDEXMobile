package com.elektro24team.auravindex.model

data class Book(
    val id: String,
    val title: String,
    val isbn: String,
    val classification: String,
    val summary: String,
    val editorial: String,
    val language: String,
    val edition: String,
    val sample: String,
    val location: String,
    val book_status: String,
    val genres: List<String>,
    val book_collection: String,
    val authors: List<String>,
    val book_img: String
)