package com.me.daggersample.di

import com.me.daggersample.newsListing.NewsListingFragment
import com.me.daggersample.newsListing.NewsListingModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector
    internal abstract fun provideNewsListingFragment(): NewsListingFragment

}