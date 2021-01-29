package com.me.daggersample.model.base

import com.google.gson.annotations.SerializedName

data class ApiErrorResponse(
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("code")
    val code: String? = null
)