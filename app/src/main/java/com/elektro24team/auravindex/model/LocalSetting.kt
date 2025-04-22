package com.elektro24team.auravindex.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "localsettings")
data class LocalSetting(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val userEmail: String,
    val lastLoggedIn: Long,
    val language: String,
    val darkMode: Boolean,
    val creationDate: Long = System.currentTimeMillis()
)