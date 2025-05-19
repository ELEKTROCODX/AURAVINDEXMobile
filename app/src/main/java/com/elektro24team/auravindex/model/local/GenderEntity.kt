package com.elektro24team.auravindex.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genders")
data class GenderEntity(
    val __v: Int,
    @PrimaryKey val _id: String,
    val createdAt: String,
    val name: String,
    val updatedAt: String
)