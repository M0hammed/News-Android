package com.me.daggersample

import com.me.daggersample.app.DaggerSampleApplication
import com.me.daggersample.di.AppComponent
import com.me.daggersample.di.DaggerAppComponent


class TestApp : DaggerSampleApplication() {

    override fun initAppComponent(): AppComponent {
        return DaggerAppComponent.factory().create(this)
    }
}