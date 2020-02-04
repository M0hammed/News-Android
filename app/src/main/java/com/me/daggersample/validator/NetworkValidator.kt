package com.me.daggersample.validator

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.me.daggersample.utils.Platform

class NetworkValidator(private val application: Application) : INetworkValidator {
    override fun isConnected(): Boolean {
        val connectivityManager = (application
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

        if (Platform.isAndroidM()) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val activeNetInfo = connectivityManager.activeNetworkInfo
            return activeNetInfo != null && activeNetInfo.isConnected
        }
    }
}