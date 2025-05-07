package com.elektro24team.auravindex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elektro24team.auravindex.model.local.BookCollectionEntity

@Dao
interface BookCollectionDao {
    @Query("SELECT * FROM bookcollections")
    suspend fun getAllBookCollections(): List<BookCollectionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookCollections(bookCollections: List<BookCollectionEntity>)

    @Query("DELETE FROM bookcollections")
    suspend fun clearBookCollections()
}