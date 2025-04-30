// AuraVindexApp.kt
package com.elektro24team.auravindex

import android.app.Application
import com.elektro24team.auravindex.utils.NetworkLiveData

class AuraVindexApp : Application() {
    val networkLiveData: NetworkLiveData by lazy {
        NetworkLiveData(this)
    }
}