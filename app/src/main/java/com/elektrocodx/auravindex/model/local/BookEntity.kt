package com.elektrocodx.auravindex.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "books")
data class BookEntity (
    val __v: Int,
    @PrimaryKey val _id: String,
    val book_collection_id: String,
    val book_img: String,
    val book_status_id: String,
    val classification: String,
    val createdAt: String,
    val edition: String,
    val editorial_id: String,
    val authors_id: List<String>,
    val genres: List<String>,
    val isbn: String,
    val language: String,
    val location: String,
    val sample: String,
    val summary: String,
    val title: String,
    val updatedAt: String
)