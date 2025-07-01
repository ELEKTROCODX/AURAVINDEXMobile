package com.elektrocodx.auravindex.utils.enums

enum class AppAction(val appActionTitle: String) {
    SUBSCRIBE_TO_PLAN("subscribe to a plan"),
    RENEW_ACTIVE_PLAN("renew your plan"),
    CANCEL_ACTIVE_PLAN("cancel your plan"),
    LOAN_BOOK("loan a book"),
    ACCESS_LOANS("access your loans"),
    CHECK_LISTS("check your lists"),
    ACCESS_ADMIN_DASHBOARD("access the admin dashboard"),
    ACCESS_PROFILE_PAGE("access the profile page"),
    CREATE_LIST("create a list"),
    ADD_BOOK_TO_LIST("add a book to a list"),;

    companion object {
        fun fromKey(appActionTitle: String): AppAction? =
            AppAction.values().firstOrNull { it.appActionTitle == appActionTitle }
    }
}