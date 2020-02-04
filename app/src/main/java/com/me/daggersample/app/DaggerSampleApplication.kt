package com.me.daggersample.app

import android.app.Application

class DaggerSampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initAppComponent()
    }

    private fun initAppComponent() {
    }
}