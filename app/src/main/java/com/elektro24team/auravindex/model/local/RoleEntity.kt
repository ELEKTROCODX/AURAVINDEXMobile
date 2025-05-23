package com.elektro24team.auravindex.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roles")
data class RoleEntity(
    val __v: Int,
    @PrimaryKey val _id: String,
    val createdAt: String,
    val name: String,
    val permissions: List<String>,
    val updatedAt: String
)