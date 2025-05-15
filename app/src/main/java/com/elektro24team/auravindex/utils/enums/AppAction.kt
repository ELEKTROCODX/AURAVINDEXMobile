package com.elektro24team.auravindex.utils.enums

enum class AppAction(val appActionTitle: String) {
    SUBSCRIBE_TO_PLAN("subscribe to a plan"),
    CHECK_LISTS("check your lists"),
    ACCESS_ADMIN_DASHBOARD("access the admin dashboard"),;

    companion object {
        fun fromKey(appActionTitle: String): AppAction? =
            AppAction.values().firstOrNull { it.appActionTitle == appActionTitle }
    }
}