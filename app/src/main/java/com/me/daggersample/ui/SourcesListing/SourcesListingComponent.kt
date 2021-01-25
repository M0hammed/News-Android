package com.me.daggersample.ui.SourcesListing

import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [SourcesListingModule::class])
interface SourcesListingComponent {

    fun inject(newsListingFragment: SourcesListingFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): SourcesListingComponent
    }
}