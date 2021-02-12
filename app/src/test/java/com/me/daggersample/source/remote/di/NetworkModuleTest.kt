package com.me.daggersample.source.remote.di

import com.me.daggersample.utils.Platform
import io.mockk.every
import io.mockk.mockkObject
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkModuleTest {

    @Test
    fun `given buildConfig is not debug when provideHttpInterceptor level then return logging is NONE`() {
        // GIVEN
        mockkObject(Platform)
        every { Platform.isBuildConfigDebug() } returns (false)

        // THEN
        val networkModule = NetworkModule()
        assertEquals(
            networkModule.provideHttpInterceptor().level,
            HttpLoggingInterceptor.Level.NONE
        )
    }
}
