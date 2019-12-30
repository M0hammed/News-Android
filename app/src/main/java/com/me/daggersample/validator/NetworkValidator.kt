package com.me.daggersample.validator

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager

class NetworkValidator(private val application: Application) : INetworkValidator {
    override fun isConnected(): Boolean {
        val connectivityManager = (application
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return activeNetInfo != null && activeNetInfo.isConnected
    }
}