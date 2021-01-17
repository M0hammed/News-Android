package com.me.daggersample.model.base

sealed class ErrorTypes(
    val errorTitle: StringModel? = null,
    val errorSubTitle: StringModel? = null,
    val errorIcon: Int? = null,
) {
    object NoData : ErrorTypes()
    object ServerError : ErrorTypes()
    object NoNetwork : ErrorTypes()
    data class GeneralError(val cause: Throwable? = null) : ErrorTypes()
    object ApiFail : ErrorTypes()
}