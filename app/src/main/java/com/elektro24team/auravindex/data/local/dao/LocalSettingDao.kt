package com.elektro24team.auravindex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elektro24team.auravindex.model.local.LocalSettingEntity

@Dao
interface LocalSettingDao {
    @Query("SELECT * FROM localsettings WHERE keySetting = :key LIMIT 1")
    suspend fun getSetting(key: String): LocalSettingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSetting(setting: LocalSettingEntity)
}
