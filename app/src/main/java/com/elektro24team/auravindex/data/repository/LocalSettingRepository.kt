package com.elektro24team.auravindex.data.repository

import com.elektro24team.auravindex.data.local.dao.LocalSettingDao
import com.elektro24team.auravindex.model.LocalSetting
import kotlinx.coroutines.flow.Flow

class LocalSettingRepository(private val localSettingDao: LocalSettingDao){
    suspend fun insert(localsetting: LocalSetting) {
        localSettingDao.insert(localsetting)
    }

    suspend fun update(localsetting: LocalSetting){
        localSettingDao.update(localsetting)
    }

    suspend fun delete(localsetting: LocalSetting){
        localSettingDao.delete(localsetting)
    }

    var allLocalSettings: Flow<List<LocalSetting>> = localSettingDao.getAllLocalSettings()

    suspend fun getLocalSettingById(id: Int): LocalSetting? {
        return localSettingDao.getLocalSettingById(id)
    }
    suspend fun getLocalSettingByUserId(userId: String): LocalSetting? {
        return localSettingDao.getLocalSettingByUserId(userId)
    }

}