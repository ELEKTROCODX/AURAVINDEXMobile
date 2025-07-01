package com.elektrocodx.auravindex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elektrocodx.auravindex.model.local.AuthorEntity

@Dao
interface AuthorDao {
    @Query("SELECT * FROM authors")
    suspend fun getAll(): List<AuthorEntity>

    @Query("SELECT * FROM authors WHERE _id = :id")
    suspend fun getById(id: String): AuthorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(authors: List<AuthorEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(author: AuthorEntity)

    @Query("DELETE FROM authors")
    suspend fun deleteAll()
}