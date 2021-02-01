package com.me.daggersample.utils

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import java.io.InputStream

fun getStringJson(path: String): String {
    val ctx: Context = InstrumentationRegistry.getInstrumentation().targetContext
    val inputStream: InputStream = ctx.classLoader.getResourceAsStream(path)
    return inputStream.bufferedReader().use { it.readText() }
}