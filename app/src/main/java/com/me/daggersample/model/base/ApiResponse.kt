package com.me.daggersample.model.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class ApiResponse<T> {
    @SerializedName("status")
    var status: String? = null
    @SerializedName("message")
    var message: String? = null
    @SerializedName("sources")
    var result: T? = null
    @SerializedName("totalcount")
    var totalcount: Int = 0
}