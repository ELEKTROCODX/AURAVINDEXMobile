package com.elektrocodx.auravindex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elektrocodx.auravindex.model.local.BookStatusEntity

@Dao
interface BookStatusDao {
    @Query("SELECT * FROM bookstatuses")
    suspend fun getAll(): List<BookStatusEntity>

    @Query("SELECT * FROM bookstatuses WHERE _id = :id")
    suspend fun getById(id: String): BookStatusEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(book_statuses: List<BookStatusEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book_status: BookStatusEntity)

    @Query("DELETE FROM bookstatuses")
    suspend fun deleteAll()
}