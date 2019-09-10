package com.me.daggersample.messageHandler

import android.content.Context
import com.me.daggersample.R
import com.me.daggersample.common.InternetConnectionError
import com.me.daggersample.common.NO_DATA
import com.me.daggersample.common.ServerError
import com.me.daggersample.data.networkData.ErrorResponse

class ErrorMessageHandler(private val context: Context) {
    fun getErrorMessage(errorModel: ErrorResponse): String =
        when (errorModel.code) {
            ServerError -> context.getString(R.string.server_error)
            InternetConnectionError -> context.getString(R.string.internet_connection_error)
            NO_DATA -> context.getString(R.string.no_data)
            else -> context.getString(R.string.server_error)
        }
}