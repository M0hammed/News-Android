package com.me.daggersample.data.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class ApiResponse<T> {
    @SerializedName("status")
    @Expose
    var status: Int = 0
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("result")
    @Expose
    var result: T? = null
}