package com.elektro24team.auravindex

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import com.elektro24team.auravindex.model.local.NotificationEntity
import com.elektro24team.auravindex.utils.objects.AuthPrefsHelper
import com.elektro24team.auravindex.utils.objects.FcmTokenUploader
import com.elektro24team.auravindex.utils.objects.NotificationHandler
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
        Log.d("FCM", "New Token: $token")
        CoroutineScope(Dispatchers.IO).launch {
            AuthPrefsHelper.saveFcmToken(context = applicationContext, token)
            FcmTokenUploader.updateFcmTokenIfNeeded(applicationContext, token)
        }
    }
}