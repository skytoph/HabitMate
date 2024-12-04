package com.github.skytoph.taski.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

interface NetworkManager {
    fun isNetworkAvailable(): Boolean

    class Base(private val context: Context) : NetworkManager {
        override fun isNetworkAvailable(): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities = connectivityManager.activeNetwork?.let { connectivityManager.getNetworkCapabilities(it) }
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
        }
    }
}
