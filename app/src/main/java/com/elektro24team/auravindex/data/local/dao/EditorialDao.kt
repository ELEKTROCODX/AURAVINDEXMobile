package com.elektro24team.auravindex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elektro24team.auravindex.model.local.EditorialEntity

@Dao
interface EditorialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEditorials(editorials: List<EditorialEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEditorial(editorial: EditorialEntity)

    @Query("SELECT * FROM editorials")
    suspend fun getAllEditorials(): List<EditorialEntity>

    @Query("SELECT * FROM editorials WHERE _id = :editorialId LIMIT 1")
    suspend fun getEditorialById(editorialId: String): EditorialEntity?

    @Query("DELETE FROM editorials")
    suspend fun clearEditorials()
}