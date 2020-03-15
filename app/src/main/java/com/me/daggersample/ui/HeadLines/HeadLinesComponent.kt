package com.me.daggersample.ui.HeadLines

import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [HeadLinesModule::class])
interface HeadLinesComponent {
    fun inject(headLinesFragment: HeadLinesFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance sourceId: String?): HeadLinesComponent
    }
}