package com.me.daggersample.network.handler

import com.me.daggersample.common.InternetConnectionError
import com.me.daggersample.common.ServerError
import com.me.daggersample.data.networkData.ErrorResponse
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.UnknownHostException

class NetworkExceptionHandler(private val throwable: Throwable) {
    fun getFailureException(): ErrorResponse =
        when (throwable) {
            is UnknownHostException, is NoRouteToHostException -> ErrorResponse(ServerError)
            is ConnectException -> ErrorResponse(InternetConnectionError)
            else -> ErrorResponse(InternetConnectionError)
        }
}