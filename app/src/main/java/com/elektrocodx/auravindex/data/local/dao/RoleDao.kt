package com.elektrocodx.auravindex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.elektrocodx.auravindex.model.local.RoleEntity

@Dao
interface RoleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoles(roles: List<RoleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRole(role: RoleEntity)

    @Query("SELECT * FROM roles")
    suspend fun getAllRoles(): List<RoleEntity>

    @Query("SELECT * FROM roles WHERE _id = :roleId LIMIT 1")
    suspend fun getRoleById(roleId: String): RoleEntity?

    @Query("DELETE FROM roles")
    suspend fun clearRoles()
}