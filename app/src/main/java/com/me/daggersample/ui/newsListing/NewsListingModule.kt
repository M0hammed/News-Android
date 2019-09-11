package com.me.daggersample.ui.newsListing

import com.me.daggersample.network.apiInterface.NewsListingApiInterface
import dagger.Module
import dagger.Provides

@Module
class NewsListingModule {

    @Provides
    internal fun providesNewsListingRepository(newsListingApiInterface: NewsListingApiInterface): NewsListingRepository =
        NewsListingRepository(newsListingApiInterface)


    @Provides
    internal fun providesNewsListingViewModelProvider(newsListingRepository: NewsListingRepository) =
        NewsListingViewModelFactory(newsListingRepository)
}