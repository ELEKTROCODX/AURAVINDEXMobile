package com.elektro24team.auravindex.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "logactions")
data class LogActionEntity(
    val __v: Int,
    @PrimaryKey val _id: String,
    val action_code: String,
    val createdAt: String,
    val updatedAt: String
)