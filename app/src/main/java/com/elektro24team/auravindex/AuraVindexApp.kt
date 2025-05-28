// AuraVindexApp.kt
package com.elektro24team.auravindex

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.elektro24team.auravindex.utils.NetworkLiveData

class AuraVindexApp : Application() {
    val networkLiveData: NetworkLiveData by lazy {
        NetworkLiveData(this)
    }

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Aura Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("aura_channel", name, importance)
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}