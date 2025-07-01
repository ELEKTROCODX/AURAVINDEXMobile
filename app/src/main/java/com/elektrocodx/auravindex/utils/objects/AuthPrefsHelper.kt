package com.elektrocodx.auravindex.utils.objects

import android.content.Context

object AuthPrefsHelper {
    private const val PREFS_NAME = "auth_prefs"
    private const val TOKEN_KEY = "auth_token"
    private const val FCM_TOKEN_KEY = "fcm_token"
    private const val USER_ID = "user_id"
    private const val NOTIFICATION_PERMISION_REQUESTED = "notification_permission_requested"
    fun saveAuthToken(context: Context, token: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }
    fun getAuthToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(TOKEN_KEY, null)
    }
    fun clearAuthToken(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(TOKEN_KEY).apply()
    }
    fun saveFcmToken(context: Context, token: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(FCM_TOKEN_KEY, token).apply()
    }
    fun getFcmToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(FCM_TOKEN_KEY, null)
    }
    fun clearFcmToken(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(FCM_TOKEN_KEY).apply()
    }
    fun saveUserId(context: Context, userId: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(USER_ID, userId).apply()
    }
    fun getUserId(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(USER_ID, null)
    }
    fun clearUserId(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(USER_ID).apply()
    }
    fun hasRequestedPermission(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(NOTIFICATION_PERMISION_REQUESTED, false)
    }
    fun setPermissionRequested(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(NOTIFICATION_PERMISION_REQUESTED, true).apply()
    }
    fun clearPermissionRequested(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().remove(NOTIFICATION_PERMISION_REQUESTED).apply()
    }
}