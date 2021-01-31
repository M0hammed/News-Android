package com.me.daggersample.di

import com.me.daggersample.ui.SourcesListing.di.SourcesListingComponent
import dagger.Module

@Module(subcomponents = [SourcesListingComponent::class])
class AppSubComponentModule