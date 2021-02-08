package com.me.daggersample.source.remote.di

import com.me.daggersample.utils.Platform
import io.mockk.every
import io.mockk.mockkObject
import org.junit.Assert.*
import org.junit.Test

class UrlModuleTest {

    @Test
    fun `given BuildConfig when providesBaseUrl then verify correct base url`() {
        // GIVEN
        val baseUrl = "https://newsapi.org/v2/"
        mockkObject(Platform)
        every { Platform.baseUrl() } returns (baseUrl)

        // THEN
        val urlModule = UrlModule()
        assertEquals(urlModule.providesBaseUrl(), baseUrl)
    }
}