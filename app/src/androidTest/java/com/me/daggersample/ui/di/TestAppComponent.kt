package com.me.daggersample.ui.di

import android.content.Context
import com.me.daggersample.di.AppComponent
import com.me.daggersample.di.AppSubComponentModule
import com.me.daggersample.source.remote.apiClient.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestUrlModule::class, NetworkModule::class, AppSubComponentModule::class])
interface TestAppComponent : AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): TestAppComponent
    }
}