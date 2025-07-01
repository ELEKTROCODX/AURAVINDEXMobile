package com.elektrocodx.auravindex.data.repository

import com.elektrocodx.auravindex.data.local.dao.LocalSettingDao
import com.elektrocodx.auravindex.model.local.LocalSettingEntity

class LocalSettingRepository(private val dao: LocalSettingDao) {

    suspend fun getSetting(keySetting: String): String? {
        return dao.getSetting(keySetting)?.keyValue
    }

    suspend fun setSetting(keySetting: String, keyValue: String) {
        dao.upsertSetting(LocalSettingEntity(keySetting, keyValue))
    }

    suspend fun clearSetting(keySetting: String) {
        dao.deleteSetting(keySetting)
    }

}