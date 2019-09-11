package com.me.daggersample.di

import com.me.daggersample.ui.newsListing.NewsListingActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [FragmentBuilder::class])
    internal abstract fun provideNewsListingActivity(): NewsListingActivity
}