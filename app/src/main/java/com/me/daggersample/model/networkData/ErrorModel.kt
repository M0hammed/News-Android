package com.me.daggersample.model.networkData

data class ErrorModel(
    var serverMessage: String? = null,
    var message: Int = 0,
    var subMessage: Int = 0,
    var errorIcon: Int = 0,
    var errorStatus: ErrorStatus? = null
)