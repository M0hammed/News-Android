package com.me.daggersample.model.base

sealed class ErrorTypes(
    val errorTitle: String? = null,
    val errorSubTitle: String? = null,
    val errorIcon: Int? = null,
) {
    object NoData : ErrorTypes("No data found")

    data class ServerError(val apiErrorResponse: ApiErrorResponse?) :
        ErrorTypes(errorTitle = apiErrorResponse?.message)

    object NoNetwork : ErrorTypes("No network")

    data class UnknownError(val cause: Throwable? = null) : ErrorTypes("Something Wrong")
}