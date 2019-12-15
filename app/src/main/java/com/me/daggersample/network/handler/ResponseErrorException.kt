package com.me.daggersample.network.handler

import com.me.daggersample.data.networkData.ErrorResponse

class ResponseErrorException(val errorModel: ErrorResponse) : Exception()
