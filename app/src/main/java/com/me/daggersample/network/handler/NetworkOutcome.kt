package com.me.daggersample.network.handler

import com.me.daggersample.data.networkData.ErrorResponse

data class NetworkOutcome<T>(
    var isSuccess: Boolean,
    var body: T?,
    var responseError:ErrorResponse
)