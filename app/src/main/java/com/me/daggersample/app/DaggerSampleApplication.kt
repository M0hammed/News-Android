package com.me.daggersample.app

import android.app.Application
import com.me.daggersample.di.AppComponent
import com.me.daggersample.di.DaggerAppComponent

class DaggerSampleApplication : Application() {
    private lateinit var _appComponent: AppComponent
    val appComponent
        get() = _appComponent

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    private fun initAppComponent() {
        _appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}