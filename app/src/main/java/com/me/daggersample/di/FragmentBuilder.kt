package com.me.daggersample.di

import com.me.daggersample.ui.newsListing.NewsListingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    internal abstract fun provideNewsListingFragment(): NewsListingFragment

}