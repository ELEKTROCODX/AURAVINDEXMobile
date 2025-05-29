package com.elektro24team.auravindex

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.annotation.RequiresPermission
import com.elektro24team.auravindex.data.local.AuraVindexDatabase
import com.elektro24team.auravindex.data.repository.PlanRepository
import com.elektro24team.auravindex.navigation.NavGraph
import com.elektro24team.auravindex.ui.theme.AppTheme
import com.elektro24team.auravindex.utils.objects.NotificationHandler.createNotificationChannel
import com.elektro24team.auravindex.viewmodels.PlanViewModel

class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @RequiresApi(Build.VERSION_CODES.O)
    @RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
    override fun onCreate(savedInstanceState: Bundle?) {
        createNotificationChannel(this)
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                NavGraph()
            }
        }
    }
}