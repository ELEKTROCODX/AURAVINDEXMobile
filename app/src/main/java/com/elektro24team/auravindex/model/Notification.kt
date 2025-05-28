package com.elektro24team.auravindex.model

data class Notification(
    val __v: Int,
    val _id: String,
    val receiver: User,
    val title: String,
    val message: String,
    val notification_type: String,
    val is_read: Boolean,
    val createdAt: String,
    val updatedAt: String
)