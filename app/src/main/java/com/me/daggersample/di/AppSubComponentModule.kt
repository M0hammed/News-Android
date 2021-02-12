package com.me.daggersample.di

import com.me.daggersample.ui.HeadLines.di.HeadLinesComponent
import com.me.daggersample.ui.SourcesListing.di.SourcesListingComponent
import dagger.Module

@Module(subcomponents = [SourcesListingComponent::class, HeadLinesComponent::class])
class AppSubComponentModule
