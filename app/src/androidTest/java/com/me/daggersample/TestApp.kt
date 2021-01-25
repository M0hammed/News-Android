package com.me.daggersample

import com.me.daggersample.app.DaggerSampleApplication
import com.me.daggersample.di.AppComponent
import com.me.daggersample.ui.di.DaggerTestAppComponent


class TestApp : DaggerSampleApplication() {

    override fun initAppComponent(): AppComponent {
        return DaggerTestAppComponent.factory().create(this)
    }
}