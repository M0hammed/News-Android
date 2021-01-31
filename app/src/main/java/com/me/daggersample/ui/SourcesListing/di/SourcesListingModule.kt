package com.me.daggersample.ui.SourcesListing.di

import com.me.daggersample.ui.SourcesListing.data.ISourcesListingRepository
import com.me.daggersample.ui.SourcesListing.data.SourcesListingRepository
import dagger.Binds
import dagger.Module

@Module
abstract class SourcesListingModule {
    @Binds
    abstract fun provideISourcesListingRepository(sourcesListingRepository: SourcesListingRepository): ISourcesListingRepository
}