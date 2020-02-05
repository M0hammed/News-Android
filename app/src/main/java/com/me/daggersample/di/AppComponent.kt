package com.me.daggersample.di

import android.app.Application
import com.me.daggersample.source.remote.apiClient.NetworkModule
import com.me.daggersample.ui.newsListing.NewsListingComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    fun getNewsListingComponentBuilder(): NewsListingComponent.Builder

    @Component.Builder
    interface Builder {
        fun application(@BindsInstance application: Application): Builder

        fun build(): AppComponent
    }
}