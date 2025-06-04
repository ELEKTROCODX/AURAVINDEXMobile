package com.elektro24team.auravindex.utils.objects

import android.content.Context
import android.util.Log
import android.widget.Toast
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
                } else {
                    //Toast.makeText(context, "Failed to update FCM token.", Toast.LENGTH_SHORT).show()
                    Log.d("FcmTokenUploader", "Failed to update FCM token: ${response.errorBody()?.string()}")
                }
            } else {
                //Toast.makeText(context, "Auth token not available.", Toast.LENGTH_SHORT).show()
                Log.d("FcmTokenUploader", "Auth token not available.")
            }
        } catch (e: Exception) {
            Log.d("FcmTokenUploader", "Error updating FCM token: ${e.message}")
            //Toast.makeText(context, "Something failed: {${e.message}}", Toast.LENGTH_SHORT).show()
        }
    }
     fun checkAndSyncFcmToken(context: Context) {
         FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
             if (!task.isSuccessful) {
                 return@addOnCompleteListener
             }
             val token = task.result
             val savedToken = AuthPrefsHelper.getFcmToken(context)
             if (token != savedToken) {
                 AuthPrefsHelper.saveFcmToken(context, token)
                 val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
                 applicationScope.launch {
                     updateFcmTokenIfNeeded(context, token)
                 }
             }
         }
    }
}