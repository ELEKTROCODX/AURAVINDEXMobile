package com.elektrocodx.auravindex.utils.classes

import androidx.compose.ui.graphics.vector.ImageVector
import com.elektrocodx.auravindex.utils.enums.AdminDashboardObject

data class AdminMenuItem (
    val title: String,
    val icon: ImageVector,
    val dashboardObject: AdminDashboardObject
)