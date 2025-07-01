package com.elektrocodx.auravindex.utils.classes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoFixOff
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.ui.graphics.vector.ImageVector

data class BookStatusIcon(
    val book_status: String,
    val icon: ImageVector
)
val bookStatusIcons = listOf(
    BookStatusIcon("AVAILABLE", Icons.Default.CheckCircle),
    BookStatusIcon("NOT AVAILABLE", Icons.Default.Block),
    BookStatusIcon("LENT", Icons.Default.AccessTime),
    BookStatusIcon("RESERVED", Icons.Default.LockClock),
    BookStatusIcon("REPAIRING", Icons.Default.AutoFixOff),
    BookStatusIcon("DISCARDED", Icons.Default.RestoreFromTrash),
)
