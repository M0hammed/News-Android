package com.me.daggersample.di

import android.app.Application
import com.me.daggersample.app.DaggerSampleApplication
import com.me.daggersample.source.remote.apiClient.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}