package com.me.daggersample.ui.TeamsListing

import dagger.Subcomponent

@Subcomponent(modules = [TeamsListingModule::class])
interface TeamsListingComponent {

    fun inject(newsListingFragment: TeamsListingFragment)

    @Subcomponent.Builder
    interface Builder {
        fun build(): TeamsListingComponent
    }
}