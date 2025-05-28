package com.elektro24team.auravindex.model.local

data class NotificationEntity(
    val __v: Int,
    val _id: String,
    val receiver_id: String,
    val title: String,
    val message: String,
    val notificationType: String,
    val isRead: Boolean,
    val createdAt: String,
    val updatedAt: String
)