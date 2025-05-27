package com.elektro24team.auravindex.utils.enums

enum class SettingKey(val keySetting: String) {
    ID("userId"),
    EMAIL("userEmail"),
    PROFILE_IMAGE("userProfileImage"),
    TOKEN("userToken"),
    ROLE_NAME("roleName"),
    ROLE_ID("roleId"),
    ACTIVE_PLAN("activePlan"),
    ACTIVE_PLAN_ID("activePlanId"),
    ACTIVE_PLAN_ENDING_DATE("activePlanEndingDate"),
    LAST_LOGIN("lastLogin"),
    LANGUAGE("language"),
    DARK_MODE("darkMode"),
    RECEIVE_PUSH_NOTIFICATIONS("receivePushNotifications"),
    RECEIVE_EMAIL_NOTIFICATIONS("receiveEmailNotifications"),
    RECEIVE_SMS_NOTIFICATIONS("receiveSmsNotifications"),;

    companion object {
        fun fromKey(keySetting: String): SettingKey? =
            values().firstOrNull { it.keySetting == keySetting }
    }
}