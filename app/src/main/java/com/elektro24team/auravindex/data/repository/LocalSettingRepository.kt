package com.elektro24team.auravindex.data.repository

import com.elektro24team.auravindex.data.local.dao.LocalSettingDao
import com.elektro24team.auravindex.model.local.LocalSettingEntity

class LocalSettingRepository(private val dao: LocalSettingDao) {

    suspend fun getSetting(keySetting: String): String? {
        return dao.getSetting(keySetting)?.keyValue
    }

    suspend fun setSetting(keySetting: String, keyValue: String) {
        dao.upsertSetting(LocalSettingEntity(keySetting, keyValue))
    }
}