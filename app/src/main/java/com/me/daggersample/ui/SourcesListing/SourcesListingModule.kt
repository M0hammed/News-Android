package com.me.daggersample.ui.SourcesListing

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class SourcesListingModule {

    @Provides
    internal fun provideTeamsListingAdapter(
        context: Context, sourcesListingFragment: SourcesListingFragment
    ) = SourcesListingAdapter(context, sourcesListingFragment)
}