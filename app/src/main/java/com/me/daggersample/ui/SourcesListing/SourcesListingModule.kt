package com.me.daggersample.ui.SourcesListing

import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class SourcesListingModule {
    @Binds
    abstract fun provideISourcesListingRepository(sourcesListingRepository: SourcesListingRepository): ISourcesListingRepository
}