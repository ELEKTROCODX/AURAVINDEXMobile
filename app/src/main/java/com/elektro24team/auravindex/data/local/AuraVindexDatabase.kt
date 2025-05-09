package com.elektro24team.auravindex.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.elektro24team.auravindex.data.local.dao.BookCollectionDao
import com.elektro24team.auravindex.data.local.dao.LocalSettingDao
import com.elektro24team.auravindex.data.local.dao.PlanDao
import com.elektro24team.auravindex.model.local.BookCollectionEntity
import com.elektro24team.auravindex.model.local.LocalSettingEntity
import com.elektro24team.auravindex.model.local.PlanEntity

@Database(
    entities = [
        PlanEntity::class,
        BookCollectionEntity::class,
        LocalSettingEntity::class
               ],
    version = 3 // Note: Increase version number when database schema changes
)
abstract class AuraVindexDatabase : RoomDatabase() {
    abstract fun planDao(): PlanDao
    abstract fun bookCollectionDao(): BookCollectionDao
    abstract fun localSettingDao(): LocalSettingDao

    companion object {
        @Volatile private var INSTANCE: AuraVindexDatabase? = null

        fun getInstance(context: Context): AuraVindexDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AuraVindexDatabase::class.java,
                    "auravindex.db"
                )
                    .addMigrations(MIGRATION_2_3)
                    .build()
                    .also { INSTANCE = it }
            }
        }
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS localsettings (
                        keySetting TEXT NOT NULL PRIMARY KEY,
                        keyValue TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
    }
}