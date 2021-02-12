package com.me.daggersample.app

import android.app.Application
import com.me.daggersample.di.AppComponent
import com.me.daggersample.di.DaggerAppComponent

open class DaggerSampleApplication : Application() {
    val appComponent by lazy {
        initAppComponent()
    }

    protected open fun initAppComponent(): AppComponent {
        return DaggerAppComponent.factory().create(this)
    }
}
