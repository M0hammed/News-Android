package com.me.daggersample.model.networkData

data class ErrorResponse(var code: String = "",
                         var devDetails: String = "",
                         var stackTraceId: Long = 0,
                         var args: Map<String, Any> = HashMap())