package com.me.daggersample.ui.di

import dagger.Module
import dagger.Provides
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Named
import javax.inject.Singleton

@Module
class TestUrlModule {
    @Singleton
    @Provides
    @Named("baseUrl")
    fun providesBaseUrl(): String {
        return "http://localhost:8080/"
    }
}