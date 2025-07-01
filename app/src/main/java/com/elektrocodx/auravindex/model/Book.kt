package com.elektrocodx.auravindex.model

data class Book(
    val __v: Int,
    val _id: String,
    val authors: List<Author>,
    val book_collection: BookCollection,
    val book_img: String,
    val book_status: BookStatus,
    val classification: String,
    val createdAt: String,
    val edition: String,
    val editorial: Editorial,
    val genres: List<String>,
    val isbn: String,
    val language: String,
    val location: String,
    val sample: String,
    val summary: String,
    val title: String,
    val updatedAt: String
)