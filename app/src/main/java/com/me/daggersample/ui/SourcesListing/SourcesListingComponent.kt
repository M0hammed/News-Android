package com.me.daggersample.ui.SourcesListing

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [SourcesListingModule::class])
interface SourcesListingComponent {

    fun inject(newsListingFragment: SourcesListingFragment)

    @Subcomponent.Factory
    interface Builder {
        fun create(@BindsInstance sourcesListingFragment: SourcesListingFragment): SourcesListingComponent
    }
}