package com.me.daggersample.model.base

sealed class ErrorTypes(
    val errorTitle: String? = null,
    val errorSubTitle: String? = null,
    val errorIcon: Int? = null,
) {
    object NoData : ErrorTypes("No Data")
    object ServerError : ErrorTypes("Server Error")
    object NoNetwork : ErrorTypes()
    data class GeneralError(val cause: Throwable? = null) : ErrorTypes("Something Wrong")
    object ApiFail : ErrorTypes("Api Failed")
}