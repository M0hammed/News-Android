package com.me.daggersample.ui.newsListing

import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.validator.INetworkValidator
import dagger.Module
import dagger.Provides

@Module
class NewsListingModule {

    @Provides
    internal fun providesNewsListingRepository(
        iRemoteDataSource: IRemoteDataSource, networkValidator: INetworkValidator
    ): NewsListingRepository =
        NewsListingRepository(iRemoteDataSource, networkValidator)


    @Provides
    internal fun providesNewsListingViewModelProvider(newsListingRepository: NewsListingRepository) =
        NewsListingViewModelFactory(newsListingRepository)
}