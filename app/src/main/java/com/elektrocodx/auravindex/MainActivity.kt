package com.elektrocodx.auravindex

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.annotation.RequiresPermission
import androidx.core.view.WindowCompat
import com.elektrocodx.auravindex.navigation.NavGraph
import com.elektrocodx.auravindex.ui.theme.AppTheme
import com.elektrocodx.auravindex.utils.objects.NotificationHandler.createNotificationChannel

class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @RequiresApi(Build.VERSION_CODES.O)
    @RequiresPermission(android.Manifest.permission.POST_NOTIFICATIONS)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AppTheme {
                NavGraph()
            }
        }
    }
}