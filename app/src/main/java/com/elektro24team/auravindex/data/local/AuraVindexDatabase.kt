package com.elektro24team.auravindex.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.elektro24team.auravindex.data.local.dao.AuditLogDao
import com.elektro24team.auravindex.data.local.dao.AuthorDao
import com.elektro24team.auravindex.data.local.dao.BookCollectionDao
import com.elektro24team.auravindex.data.local.dao.BookDao
import com.elektro24team.auravindex.data.local.dao.BookStatusDao
import com.elektro24team.auravindex.data.local.dao.EditorialDao
import com.elektro24team.auravindex.data.local.dao.GenderDao
import com.elektro24team.auravindex.data.local.dao.LocalSettingDao
import com.elektro24team.auravindex.data.local.dao.LogActionDao
import com.elektro24team.auravindex.data.local.dao.PlanDao
import com.elektro24team.auravindex.data.local.dao.RoleDao
import com.elektro24team.auravindex.data.local.dao.UserDao
import com.elektro24team.auravindex.model.local.AuditLogEntity
import com.elektro24team.auravindex.model.local.AuthorEntity
import com.elektro24team.auravindex.model.local.BookAuthorCrossRef
import com.elektro24team.auravindex.model.local.BookCollectionEntity
import com.elektro24team.auravindex.model.local.BookEntity
import com.elektro24team.auravindex.model.local.BookStatusEntity
import com.elektro24team.auravindex.model.local.EditorialEntity
import com.elektro24team.auravindex.model.local.GenderEntity
import com.elektro24team.auravindex.model.local.LocalSettingEntity
import com.elektro24team.auravindex.model.local.LogActionEntity
import com.elektro24team.auravindex.model.local.PlanEntity
import com.elektro24team.auravindex.model.local.RoleEntity
import com.elektro24team.auravindex.model.local.UserEntity

@Database(
    entities = [
        PlanEntity::class,
        BookCollectionEntity::class,
        LocalSettingEntity::class,
        BookEntity::class,
        EditorialEntity::class,
        BookStatusEntity::class,
        AuthorEntity::class,
        BookAuthorCrossRef::class,
        GenderEntity::class,
        RoleEntity::class,
        UserEntity::class,
        LogActionEntity::class,
        AuditLogEntity::class,
               ],
    version = 18 // Note: Increase version number when database schema changes
    /* Note: be careful when updating schema, due local settings might be lost */
)

@TypeConverters(com.elektro24team.auravindex.utils.classes.TypeConverters::class)
internal abstract class AuraVindexDatabase : RoomDatabase() {
    abstract fun planDao(): PlanDao
    abstract fun bookCollectionDao(): BookCollectionDao
    abstract fun localSettingDao(): LocalSettingDao
    abstract fun bookDao(): BookDao
    abstract fun editorialDao(): EditorialDao
    abstract fun bookStatusDao(): BookStatusDao
    abstract fun authorDao(): AuthorDao
    abstract fun genderDao(): GenderDao
    abstract fun roleDao(): RoleDao
    abstract fun userDao(): UserDao
    abstract fun logActionDao(): LogActionDao
    abstract fun auditLogDao(): AuditLogDao


    companion object {
        @Volatile private var INSTANCE: AuraVindexDatabase? = null

        fun getInstance(context: Context): AuraVindexDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AuraVindexDatabase::class.java,
                    "auravindex.db"
                )
                    /*.addMigrations(MIGRATION_2_3)*/
                    .fallbackToDestructiveMigration(true)  /*TODO: Remove this in production*/
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