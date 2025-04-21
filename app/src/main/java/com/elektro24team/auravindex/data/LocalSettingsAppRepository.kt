package com.elektro24team.auravindex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elektro24team.auravindex.data.local.dao.LocalSettingDao
import com.elektro24team.auravindex.model.LocalSetting

@Database(entities = [LocalSetting::class], version = 1, exportSchema = false)
abstract class LocalSettingsAppDatabase : RoomDatabase(){

    abstract fun localsettingDao(): LocalSettingDao

    companion object {
        @Volatile
        private var INSTANCE: LocalSettingsAppDatabase? = null

        fun getDatabase(context: Context): LocalSettingsAppDatabase {
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalSettingsAppDatabase::class.java,
                    "localsetting_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}