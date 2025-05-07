package com.elektro24team.auravindex.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elektro24team.auravindex.data.local.dao.BookCollectionDao
import com.elektro24team.auravindex.data.local.dao.PlanDao
import com.elektro24team.auravindex.model.local.BookCollectionEntity
import com.elektro24team.auravindex.model.local.PlanEntity

@Database(
    entities = [
        PlanEntity::class,
        BookCollectionEntity::class,
               ],
    version = 1
)
abstract class AuraVindexDatabase : RoomDatabase() {
    abstract fun planDao(): PlanDao
    abstract fun bookCollectionDao(): BookCollectionDao

    companion object {
        @Volatile private var INSTANCE: AuraVindexDatabase? = null

        fun getInstance(context: Context): AuraVindexDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AuraVindexDatabase::class.java,
                    "auravindex.db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build().also { INSTANCE = it }
            }
        }
    }
}