package com.me.daggersample.app

import android.app.Application
import com.me.daggersample.di.ActivityBuilder
import com.me.daggersample.source.remote.apiClient.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, NetworkModule::class, ActivityBuilder::class])
interface AppComponent : AndroidInjector<DaggerSampleApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}