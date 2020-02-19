package com.me.daggersample.utils

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Rule
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

object RxSchedulerRule {
    val SCHEDULER_INSTANCE = Schedulers.trampoline()
}