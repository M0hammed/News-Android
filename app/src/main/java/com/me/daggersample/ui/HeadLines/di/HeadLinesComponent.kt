package com.me.daggersample.ui.HeadLines.di

import com.me.daggersample.source.remote.apiInterface.ConstantsKeys
import com.me.daggersample.ui.HeadLines.presentation.HeadLinesFragment
import dagger.BindsInstance
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [HeadLinesModule::class])
interface HeadLinesComponent {
    fun inject(headLinesFragment: HeadLinesFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(@Named(ConstantsKeys.DaggerNames.SOURCE_NAME) @BindsInstance sourceId: String?): HeadLinesComponent
    }
}