package com.me.daggersample.utils

import com.google.gson.stream.JsonReader
import java.io.File
import java.io.FileReader

object Utils {
    fun `generate json reader`(fileName: String): JsonReader {
        val file = File(fileName)
        return JsonReader(FileReader(file))
    }
}