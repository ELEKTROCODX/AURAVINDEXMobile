package com.elektro24team.auravindex.utils.objects

import android.content.Context
import android.util.Log
import com.elektro24team.auravindex.retrofit.UserClient

object FcmTokenUploader {
    suspend fun updateFcmTokenIfNeeded(context: Context, token: String) {
        try {
            val authToken = AuthPrefsHelper.getAuthToken(context)

            if (!authToken.isNullOrEmpty()) {
                val fcmRequest = mapOf("fcm_token" to token)
                val response = UserClient.apiService.updateFcmToken("Bearer $authToken", fcmRequest)
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
}