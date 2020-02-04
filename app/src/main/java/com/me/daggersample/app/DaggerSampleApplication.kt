package com.me.daggersample.app

import android.app.Application
import com.me.daggersample.di.DaggerAppComponent

class DaggerSampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    private fun initAppComponent() {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}