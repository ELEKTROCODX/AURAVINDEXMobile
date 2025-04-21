package com.elektro24team.auravindex.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.elektro24team.auravindex.model.LocalSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalSettingDao {
    @Insert
    suspend fun insert(localsetting: LocalSetting)

    @Update
    suspend fun update(localsetting: LocalSetting)

    @Delete
    suspend fun delete(localsetting: LocalSetting)

    @Query("SELECT * FROM localsettings ORDER BY userId ASC")
    fun getAllLocalSettings(): Flow<List<LocalSetting>>

    @Query("SELECT * FROM localsettings WHERE id = :id")
    suspend fun getLocalSettingById(id: Int) : LocalSetting?

    @Query("SELECT * FROM localsettings WHERE userId = :userId")
    suspend fun getLocalSettingByUserId(userId: String) : LocalSetting?

}