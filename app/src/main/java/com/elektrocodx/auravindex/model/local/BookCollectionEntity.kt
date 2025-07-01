package com.elektrocodx.auravindex.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookcollections")
data class BookCollectionEntity(
    val __v: Int,
    @PrimaryKey val _id: String,
    val createdAt: String,
    val name: String,
    val updatedAt: String
)