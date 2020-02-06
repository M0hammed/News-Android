package com.me.daggersample.ui.TeamsListing

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [TeamsListingModule::class])
interface TeamsListingComponent {

    fun inject(newsListingFragment: TeamsListingFragment)

    @Subcomponent.Factory
    interface Builder {
        fun create(
            @BindsInstance context: Context,@BindsInstance teamsListingFragment: TeamsListingFragment
        ): TeamsListingComponent
    }
}