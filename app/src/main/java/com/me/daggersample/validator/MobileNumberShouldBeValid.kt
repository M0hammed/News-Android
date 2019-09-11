package com.me.daggersample.validator

import com.me.daggersample.data.networkData.ErrorResponse

class MobileNumberShouldBeValid(private val mobileNumber: String,
                                private val key: String) : BaseValidator {
    private val allowedLength = 11
    override fun orThrow() {
        if (!isValid(mobileNumber))
            throw ResponseErrorException(ErrorResponse(key))
    }

    private fun isValid(value: String): Boolean {
        return value.matches("^[0-9]*\$".toRegex()) && value.length == allowedLength && value.startsWith("01")
    }
}