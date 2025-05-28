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
}