package com.me.daggersample.di

import android.content.Context
import com.me.daggersample.source.remote.di.NetworkModule
import com.me.daggersample.source.remote.di.UrlModule
import com.me.daggersample.ui.SourcesListing.di.SourcesListingComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [UrlModule::class, NetworkModule::class, AppSubComponentModule::class])
interface AppComponent {

    fun getNewsListingComponentFactory(): SourcesListingComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}