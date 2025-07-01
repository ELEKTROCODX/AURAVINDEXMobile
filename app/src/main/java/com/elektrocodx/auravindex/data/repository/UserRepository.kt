package com.elektrocodx.auravindex.data.repository

import com.elektrocodx.auravindex.data.local.dao.GenderDao
import com.elektrocodx.auravindex.data.local.dao.RoleDao
import com.elektrocodx.auravindex.data.local.dao.UserDao
import com.elektrocodx.auravindex.mapper.toDomain
import com.elektrocodx.auravindex.mapper.toEntity
import com.elektrocodx.auravindex.model.User
import com.elektrocodx.auravindex.retrofit.UserClient

class UserRepository(
    private val userDao: UserDao,
    private val genderDao: GenderDao,
    private val roleDao: RoleDao
){
    private val CACHE_EXPIRY_MS = 24 * 60 * 60 * 1000L // 1 día
    @Volatile
    private var lastCacheTime: Long = 0

    suspend fun getUsers(token: String): Result<List<User>> {
        return try {
            val currentTime = System.currentTimeMillis()
            val isCacheExpired = (currentTime - lastCacheTime) > CACHE_EXPIRY_MS
            val local = userDao.getAllUsersWithRelations()
            if (local.isNotEmpty() && !isCacheExpired) {
                Result.success(local.map { it.toDomain() })
            } else {
                val remote = UserClient.apiService.getUsers("Bearer $token")
                val users = remote.data ?: emptyList()
                val genders = users.mapNotNull { it.gender }.distinctBy { it._id }
                val roles = users.mapNotNull { it.role }.distinctBy { it._id }
                genderDao.insertGenders(genders.map { it.toEntity() })
                roleDao.insertRoles(roles.map { it.toEntity() })
                userDao.clearUsers()
                userDao.insertUsers(users.map { it.toEntity() })
                lastCacheTime = System.currentTimeMillis()
                Result.success(users)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun getUserById(token: String, userId: String): Result<User> {
        return try {
            val local = userDao.getUserWithRelations(userId)
            if (local != null) {
                return Result.success(local.toDomain())
            }
            val remote = UserClient.apiService.getUserById("Bearer $token", userId)
            Result.success(remote)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun saveUserToCache(user: User) {
        user.gender?.let { genderDao.insertGenders(listOf(it.toEntity())) }
        user.role?.let { roleDao.insertRoles(listOf(it.toEntity())) }
        userDao.insertUsers(listOf(user.toEntity()))
    }

    suspend fun getUser(token: String, email: String): Result<User> {
        return try {
            val response = UserClient.apiService.getUser(
                token = "Bearer $token",
                filterField = "email",
                filterValue = email
            )
            val user = response.data?.firstOrNull()
            user?.let {
                Result.success(it)
            } ?: Result.failure(Exception("No user found"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

