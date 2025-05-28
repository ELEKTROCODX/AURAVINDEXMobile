package com.elektro24team.auravindex.utils

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.elektro24team.auravindex.model.Notification
import com.elektro24team.auravindex.R

object NotificationHandler {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showNotification(context: Context, notification: Notification) {
        val channelId = "aura_channel"

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logo_app)
            .setContentTitle(notification.title)
            .setContentText(notification.message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        NotificationManagerCompat.from(context).notify(notification._id.hashCode(), builder.build())
    }
}