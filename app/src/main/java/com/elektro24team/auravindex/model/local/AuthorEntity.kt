package com.elektro24team.auravindex.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authors")
data class AuthorEntity (
    val __v: Int,
    @PrimaryKey val _id: String,
    val birthdate: String,
    val createdAt: String,
    val gender: String,
    val last_name: String,
    val name: String,
    val updatedAt: String
)