package com.me.daggersample.source.remote.handler

sealed class NetworkStatus {
    data class SUCCESS<T>(val data: T) : NetworkStatus()
    data class Error<T>(val error: T? = null) : NetworkStatus()
}