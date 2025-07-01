package com.elektrocodx.auravindex.utils

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData

class NetworkLiveData(private val app: Application) : LiveData<Boolean>() {
    private val connectivityManager = app.getSystemService(ConnectivityManager::class.java)

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            postValue(true)
        }

        override fun onLost(network: Network) {
            postValue(false)
        }
    }

    override fun onActive() {
        super.onActive()
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        postValue(capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true)

        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
