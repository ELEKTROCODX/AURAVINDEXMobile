package com.elektro24team.auravindex.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "editorials")
class EditorialEntity (
    val __v: Int,
    @PrimaryKey val _id: String,
    val address: String,
    val createdAt: String,
    val email: String,
    val name: String,
    val updatedAt: String
)