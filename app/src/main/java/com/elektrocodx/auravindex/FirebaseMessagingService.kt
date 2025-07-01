package com.elektrocodx.auravindex

import android.Manifest
import androidx.annotation.RequiresPermission
import com.elektrocodx.auravindex.model.local.NotificationEntity
import com.elektrocodx.auravindex.utils.objects.AuthPrefsHelper
import com.elektrocodx.auravindex.utils.objects.FcmTokenUploader
import com.elektrocodx.auravindex.utils.objects.NotificationHandler
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class MyFirebaseMessagingService() : FirebaseMessagingService() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: "Aura Vindex"
        val message = remoteMessage.notification?.body ?: ""

        NotificationHandler.showNotification(
            context = applicationContext,
            notification = NotificationEntity(
                _id = UUID.randomUUID().toString(),
                title = title,
                message = message,
                notificationType = "API",
                isRead = false
            )
        )
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            AuthPrefsHelper.saveFcmToken(context = applicationContext, token)
            FcmTokenUploader.updateFcmTokenIfNeeded(applicationContext, token)
        }
    }
}