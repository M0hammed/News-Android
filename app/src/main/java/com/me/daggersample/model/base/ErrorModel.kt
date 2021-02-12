package com.me.daggersample.model.base

import com.me.daggersample.R

data class ErrorModel(
    var serverMessage: String? = null,
    var message: Int = R.string.something_went_wrong,
    var subMessage: Int = R.string.please_try_again
)
