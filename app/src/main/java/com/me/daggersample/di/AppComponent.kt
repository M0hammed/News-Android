package com.me.daggersample.di

import android.app.Application
import com.me.daggersample.source.remote.apiClient.NetworkModule
import com.me.daggersample.ui.TeamsListing.TeamsListingComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    fun getNewsListingComponentBuilder(): TeamsListingComponent.Builder

    @Component.Builder
    interface Builder {
        fun application(@BindsInstance application: Application): Builder

        fun build(): AppComponent
    }
}