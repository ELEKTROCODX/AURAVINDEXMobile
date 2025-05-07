package com.elektro24team.auravindex.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "localsettings")
data class LocalSettingEntity(
    @PrimaryKey val keySetting: String,
    val keyValue: String
)