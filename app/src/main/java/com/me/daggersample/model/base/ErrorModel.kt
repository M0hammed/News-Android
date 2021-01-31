package com.me.daggersample.model.base

import android.view.View.GONE
import com.me.daggersample.R

data class ErrorModel(
    var serverMessage: String? = null,
    var message: Int = R.string.something_went_wrong,
    var subMessage: Int = R.string.please_try_again,
    var errorIcon: Int = R.drawable.like,
    var visibility: Int = GONE
)