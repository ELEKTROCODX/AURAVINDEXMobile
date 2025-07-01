package com.elektrocodx.auravindex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elektrocodx.auravindex.model.local.BookCollectionEntity

@Dao
interface BookCollectionDao {
    @Query("SELECT * FROM bookcollections")
    suspend fun getAllBookCollections(): List<BookCollectionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookCollections(bookCollections: List<BookCollectionEntity>)

    @Query("DELETE FROM bookcollections")
    suspend fun clearBookCollections()

    @Query("SELECT * FROM bookcollections WHERE _id = :bookCollectionId LIMIT 1")
    suspend fun getBookCollectionById(bookCollectionId: String): BookCollectionEntity?

}