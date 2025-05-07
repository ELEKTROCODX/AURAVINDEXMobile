package com.elektro24team.auravindex.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookcollections")
data class BookCollectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val name: String,
    val updatedAt: String
)