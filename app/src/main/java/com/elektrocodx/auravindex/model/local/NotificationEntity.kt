package com.elektrocodx.auravindex.model.local

data class NotificationEntity(
    val _id: String?,
    val receiver_id: String? = "",
    val title: String,
    val message: String,
    val notificationType: String,
    val isRead: Boolean,
)