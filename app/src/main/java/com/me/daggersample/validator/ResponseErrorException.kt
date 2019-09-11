package com.me.daggersample.validator

import com.me.daggersample.data.networkData.ErrorResponse

class ResponseErrorException(val errorModel: ErrorResponse) : Exception()
