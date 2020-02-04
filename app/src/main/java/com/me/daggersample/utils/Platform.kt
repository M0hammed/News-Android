package com.me.daggersample.utils

import android.os.Build

object Platform {
    fun isAndroidQ(): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q


    fun isAndroidN(): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

    fun isAndroidM(): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}