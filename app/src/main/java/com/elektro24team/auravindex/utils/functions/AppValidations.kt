package com.elektro24team.auravindex.utils.functions


import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.elektro24team.auravindex.utils.enums.AppAction
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun mustBeLoggedInToast(context: Context, appAction: AppAction, navController: NavController?, route: String? = "") {
    Toast.makeText(context, "You must be logged in to ${appAction.appActionTitle}.", Toast.LENGTH_SHORT).show()
    if(!route.isNullOrEmpty()) {
        navController?.navigate(route)
    }
}
fun mustBeSubscribedToast(context: Context, appAction: AppAction, navController: NavController?, route: String? = "") {
    Toast.makeText(context, "You must be subscribed to a plan to ${appAction.appActionTitle}.", Toast.LENGTH_SHORT).show()
    if(!route.isNullOrEmpty()) {
        navController?.navigate(route)
    }

}
fun isValidEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
@RequiresApi(Build.VERSION_CODES.O)
fun isNotificationRecent(createdAt: String): Boolean {
    return try {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val notificationTime = ZonedDateTime.parse(createdAt, formatter)
        val now = ZonedDateTime.now(ZoneId.systemDefault())
        val daysBetween = ChronoUnit.DAYS.between(notificationTime, now)
        daysBetween < 5
    } catch (e: Exception) {
        false
    }
}

fun checkAndRequestNotificationPermission(
    context: Context,
    activity: Activity,
    permissionLauncher: ActivityResultLauncher<String>
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permission = android.Manifest.permission.POST_NOTIFICATIONS
        val isGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        if (!isGranted) {
            permissionLauncher.launch(permission)
        }
    }
}
fun shouldRequestNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    } else {
        false
    }
}