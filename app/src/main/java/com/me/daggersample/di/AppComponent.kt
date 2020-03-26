package com.me.daggersample.di

import android.content.Context
import com.me.daggersample.source.remote.apiClient.NetworkModule
import com.me.daggersample.ui.HeadLines.HeadLinesComponent
import com.me.daggersample.ui.SourcesListing.SourcesListingComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    fun getNewsListingComponentBuilder(): SourcesListingComponent.Builder
    fun getHeadLinesComponentFactory(): HeadLinesComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}