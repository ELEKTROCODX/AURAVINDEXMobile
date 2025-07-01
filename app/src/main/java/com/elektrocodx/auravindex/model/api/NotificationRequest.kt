package com.elektrocodx.auravindex.model.api

data class NotificationRequest (
    val receiver: String,
    val title: String,
    val message: String,
    val notification_type: String,
    val is_read: Boolean,
)