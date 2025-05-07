package com.elektro24team.auravindex.utils

enum class SettingKey(val keySetting: String) {
    ID("userId"),
    EMAIL("userEmail"),
    PROFILE_IMAGE("userProfileImage"),
    TOKEN("userToken"),
    LAST_LOGIN("lastLogin"),
    LANGUAGE("language"),
    DARK_MODE("darkMode");

    companion object {
        fun fromKey(keySetting: String): SettingKey? =
            values().firstOrNull { it.keySetting == keySetting }
    }
}