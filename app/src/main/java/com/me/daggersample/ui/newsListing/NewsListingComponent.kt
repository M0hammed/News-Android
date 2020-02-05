package com.me.daggersample.ui.newsListing

import dagger.Subcomponent

@Subcomponent(modules = [NewsListingModule::class])
interface NewsListingComponent {

    fun inject(newsListingFragment: NewsListingFragment)

    @Subcomponent.Builder
    interface Builder {
        fun build(): NewsListingComponent
    }
}