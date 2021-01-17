package com.me.daggersample.source.remote.handler

import androidx.annotation.StringRes
import com.me.daggersample.model.base.ErrorTypes

sealed class Status<out T> {
    data class Success<T>(
        val data: T? = null,
        @StringRes val message: Int = STATUS_NO_MESSAGE,
        val serverMessage: String = STATUS_NO_SERVER_MESSAGE,
        @StringRes val subMessage: Int = STATUS_NO_MESSAGE
    ) : Status<T>()

    data class Error(val errorTypes: ErrorTypes) : Status<Nothing>()

    object Idle : Status<Nothing>()

    companion object {
        const val STATUS_NO_MESSAGE = -1
        const val STATUS_NO_SERVER_MESSAGE = ""
    }
}