package com.elektro24team.auravindex

import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: "Aura Vindex"
        val message = remoteMessage.notification?.body ?: ""

        NotificationHandler.showNotification(
            context = applicationContext,
            notification = NotificationEntity(
                id = UUID.randomUUID().toString(),
                title = title,
                message = message,
                type = "API",
                timestamp = System.currentTimeMillis(),
                isRead = false
            )
        )
    }
}

private fun FirebaseMessagingService.onNewToken(string: String) {}
