package com.elektrocodx.auravindex.model

data class AuditLog(
    val __v: Int,
    val _id: String,
    val action: LogAction,
    val affected_object: String,
    val createdAt: String,
    val updatedAt: String,
    val user: User?
)