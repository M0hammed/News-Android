package com.me.daggersample.ui.SourcesListing.di

import com.me.daggersample.ui.SourcesListing.presentation.SourcesListingFragment
import dagger.Subcomponent

@Subcomponent(modules = [SourcesListingModule::class])
interface SourcesListingComponent {

    fun inject(newsListingFragment: SourcesListingFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): SourcesListingComponent
    }
}