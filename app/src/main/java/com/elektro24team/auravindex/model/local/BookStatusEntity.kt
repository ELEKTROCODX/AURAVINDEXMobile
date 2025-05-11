package com.elektro24team.auravindex.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookstatuses")
class BookStatusEntity (
    val __v: Int,
    @PrimaryKey val _id: String,
    val book_status: String,
    val createdAt: String,
    val updatedAt: String
)