package com.elektro24team.auravindex.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auditlogs")
data class AuditLogEntity(
    val __v: Int,
    @PrimaryKey val _id: String,
    val createdAt: String,
    val updatedAt: String,
    val user_id: String?,
    val gender_id: String?,
    val role_id: String?,
    val action_id: String,
    val affectedObject: String
)