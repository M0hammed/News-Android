package com.me.daggersample.di

import com.me.daggersample.ui.HeadLines.HeadLinesComponent
import com.me.daggersample.ui.SourcesListing.SourcesListingComponent
import dagger.Module

@Module(subcomponents = [SourcesListingComponent::class, HeadLinesComponent::class])
class AppSubComponentModule