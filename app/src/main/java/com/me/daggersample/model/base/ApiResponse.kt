package com.me.daggersample.model.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class ApiResponse<T> {
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("sources")
    @Expose
    var result: T? = null
    @SerializedName("totalcount")
    @Expose
    var totalcount: Int = 0
}