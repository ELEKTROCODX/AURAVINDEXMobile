package com.elektro24team.auravindex.utils.objects

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elektro24team.auravindex.data.repository.AuthRepository
import com.elektro24team.auravindex.retrofit.UserClient
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object FcmTokenUploader {
    suspend fun updateFcmTokenIfNeeded(context: Context, fcmToken: String) {
        try {
            val authToken = AuthPrefsHelper.getAuthToken(context)
            val userId = AuthPrefsHelper.getUserId(context)
            if (!authToken.isNullOrEmpty()) {
                val fcmRequest = mapOf("fcm_token" to fcmToken)
                val response = UserClient.apiService.updateFcmToken("Bearer $authToken", userId.toString(), fcmRequest)
                if (response.isSuccessful) {
                    Log.d("FCM", "FCM token updated successfully.")
                } else {
                    Log.e("FCM", "Error updating FCM token: ${response.code()}")
                }
            } else {
                Log.w("FCM", "Auth token not available.")
            }
        } catch (e: Exception) {
            Log.e("FCM", "Error sending FCM token: ${e.message}")
        }
    }
     fun checkAndSyncFcmToken(context: Context) {
         FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
             if (!task.isSuccessful) {
                 Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                 return@addOnCompleteListener
             }
             val token = task.result
             val savedToken = AuthPrefsHelper.getFcmToken(context)
             Log.d("FCM", "Saved token: $savedToken, New token: $token")
             if (token != savedToken) {
                 Log.d("FCM", "FCM token changed. Updating...")
                 AuthPrefsHelper.saveFcmToken(context, token)
                 val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
                 applicationScope.launch {
                     updateFcmTokenIfNeeded(context, token)
                 }
             }
         }
    }
}