package com.elektrocodx.auravindex.model.api

data class NotificationAllUsersRequest (
    val title: String,
    val message: String,
    val notification_type: String,
    val is_read: Boolean,
)