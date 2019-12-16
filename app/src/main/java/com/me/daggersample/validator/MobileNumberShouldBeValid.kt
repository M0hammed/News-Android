package com.me.daggersample.validator

class MobileNumberShouldBeValid(private val mobileNumber: String,
                                private val key: String) : BaseValidator {
    private val allowedLength = 11
    override fun orThrow() {

    }

    private fun isValid(value: String): Boolean {
        return value.matches("^[0-9]*\$".toRegex()) && value.length == allowedLength && value.startsWith("01")
    }
}