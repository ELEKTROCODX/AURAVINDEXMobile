// AuraVindexApp.kt
package com.elektrocodx.auravindex

import android.app.Application
import com.elektrocodx.auravindex.utils.NetworkLiveData

class AuraVindexApp : Application() {
    val networkLiveData: NetworkLiveData by lazy {
        NetworkLiveData(this)
    }
}