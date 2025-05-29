package com.elektro24team.auravindex.utils.enums

enum class AdminDashboardObject(val objectName: String) {
        BOOK("book"),
        USER("user"),
        LOAN("loan"),
        NOTIFICATION("notification"),
        FEE("fee"),
        PLAN("plan"),
        ACTIVE_PLAN("active_plan"),
        AUDIT_LOG("audit_log");

        companion object {
            fun fromKey(objectName: String): AdminDashboardObject? =
                AdminDashboardObject.values().firstOrNull { it.objectName == objectName }
        }
    }