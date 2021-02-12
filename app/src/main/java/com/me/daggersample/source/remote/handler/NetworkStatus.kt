package com.me.daggersample.source.remote.handler

import okhttp3.ResponseBody

sealed class NetworkStatus<out T> {
    data class Success<T>(val data: T) : NetworkStatus<T>()
    data class Error(val responseError: ResponseError?, val cause: Throwable?) :
        NetworkStatus<Nothing>()
}

data class ResponseError(val statusCode: Int, val errorBody: ResponseBody?)
