package com.me.daggersample.source.remote.apiClient

import com.me.daggersample.BuildConfig
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class UrlModule {

    @Singleton
    @Provides
    @Named("baseUrl")
    fun providesBaseUrl(): String = BuildConfig.BASE_URL

}