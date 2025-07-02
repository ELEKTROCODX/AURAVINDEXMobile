package com.elektrocodx.auravindex.utils.functions

import com.elektrocodx.auravindex.utils.enums.SettingKey


fun isLoggedIn(
    localSettings: Map<String, String>,
): Boolean {
    return (
            localSettings.getOrDefault(SettingKey.ID.keySetting, "").isNotEmpty() &&
                    localSettings.getOrDefault(SettingKey.EMAIL.keySetting, "").isNotEmpty() &&
                    localSettings.getOrDefault(SettingKey.TOKEN.keySetting, "").isNotEmpty()
            )
}

fun isAdmin(
    localSettings: Map<String, String>,
) : Boolean {
    return (
            (localSettings.getOrDefault(SettingKey.ROLE_NAME.keySetting, "").lowercase() == "administrator"
                            || localSettings.getOrDefault(SettingKey.ROLE_NAME.keySetting, "").lowercase() == "admin"
                    ) || isOwner(localSettings)
            ) && isLoggedIn(localSettings)
}

fun isOwner(
    localSettings: Map<String, String>
) : Boolean {
    return localSettings.getOrDefault(SettingKey.ROLE_NAME.keySetting, "").lowercase() == "owner"
}