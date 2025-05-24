package com.elektro24team.auravindex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elektro24team.auravindex.model.local.GenderEntity

@Dao
interface GenderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenders(genders: List<GenderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGender(genders: GenderEntity)

    @Query("SELECT * FROM genders")
    suspend fun getAllGenders(): List<GenderEntity>

    @Query("SELECT * FROM genders WHERE _id = :genderId LIMIT 1")
    suspend fun getGenderById(genderId: String): GenderEntity?

    @Query("DELETE FROM genders")
    suspend fun clearGenders()
}