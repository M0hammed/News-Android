package com.me.daggersample.network.handler

import androidx.annotation.StringRes

sealed class ResponseStatus<T> {
    data class Success<T>(
        val data: T? = null,
        @StringRes val message: Int = STATUS_NO_MESSAGE,
        val serverMessage: String = STATUS_NO_SERVER_MESSAGE,
        @StringRes val subMessage: Int = STATUS_NO_MESSAGE
    ) : ResponseStatus<T>()

    data class ServerError(
        val serverMessage: String = STATUS_NO_SERVER_MESSAGE,
        @StringRes val subMessage: Int = STATUS_NO_MESSAGE
    ) : ResponseStatus<Nothing>()

    data class NoData(
        @StringRes val message: Int = STATUS_NO_MESSAGE,
        @StringRes val subMessage: Int = STATUS_NO_MESSAGE
    ) : ResponseStatus<Nothing>()

    data class Error(
        @StringRes val message: Int = STATUS_NO_MESSAGE,
        @StringRes val subMessage: Int = STATUS_NO_MESSAGE
    ) : ResponseStatus<Nothing>()

    data class NoNetwork(
        @StringRes val message: Int = STATUS_NO_MESSAGE,
        @StringRes val subMessage: Int = STATUS_NO_MESSAGE
    ) : ResponseStatus<Nothing>()

    data class ApiFailed(var httpCode: Int) : ResponseStatus<Nothing>()

    companion object {
        const val STATUS_NO_MESSAGE = -1
        const val STATUS_NO_SERVER_MESSAGE = ""
    }
}