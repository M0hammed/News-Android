package com.me.daggersample.model.networkData

import android.view.View.GONE

data class ErrorModel(
    var serverMessage: String? = null,
    var message: Int = 0,
    var subMessage: Int = 0,
    var errorIcon: Int = 0,
    var visibility: Int = GONE
)