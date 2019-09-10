package com.me.daggersample.base

import android.os.Bundle
import android.os.PersistableBundle
import dagger.android.HasAndroidInjector
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity(), HasAndroidInjector {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        initComponents(savedInstanceState)
    }

    protected abstract fun initComponents(savedInstanceState: Bundle?)
}