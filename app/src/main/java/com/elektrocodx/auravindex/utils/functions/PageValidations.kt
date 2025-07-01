package com.elektrocodx.auravindex.utils.functions

fun isInAdminDashboard(currentRoute: String?): Boolean {
    return currentRoute?.startsWith("admin_dashboard") == true
}