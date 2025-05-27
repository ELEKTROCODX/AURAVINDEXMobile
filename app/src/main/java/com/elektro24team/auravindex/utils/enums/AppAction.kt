package com.elektro24team.auravindex.utils.enums

enum class AppAction(val appActionTitle: String) {
    SUBSCRIBE_TO_PLAN("subscribe to a plan"),
    RENEW_ACTIVE_PLAN("renew your plan"),
    CANCEL_ACTIVE_PLAN("cancel your plan"),
    LOAN_BOOK("loan a book"),
    CHECK_LISTS("check your lists"),
    ACCESS_ADMIN_DASHBOARD("access the admin dashboard"),
    ACCESS_PROFILE_PAGE("access the profile page"), ;

    companion object {
        fun fromKey(appActionTitle: String): AppAction? =
            AppAction.values().firstOrNull { it.appActionTitle == appActionTitle }
    }
}