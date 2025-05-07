package com.elektro24team.auravindex.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elektro24team.auravindex.model.local.PlanEntity

@Database(entities = [PlanEntity::class], version = 1)
abstract class AuraVindexDatabase : RoomDatabase() {
    abstract fun planDao(): PlanDao

    companion object {
        @Volatile private var INSTANCE: AuraVindexDatabase? = null

        fun getInstance(context: Context): AuraVindexDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AuraVindexDatabase::class.java,
                    "auravindex.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}