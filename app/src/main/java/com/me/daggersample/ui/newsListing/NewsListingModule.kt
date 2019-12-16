package com.me.daggersample.ui.newsListing

import com.me.daggersample.source.remote.apiInterface.RetrofitApisInterface
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import dagger.Module
import dagger.Provides

@Module
class NewsListingModule {

    @Provides
    internal fun providesNewsListingRepository(iRemoteDataSource: IRemoteDataSource): NewsListingRepository =
        NewsListingRepository(iRemoteDataSource)


    @Provides
    internal fun providesNewsListingViewModelProvider(newsListingRepository: NewsListingRepository) =
        NewsListingViewModelFactory(newsListingRepository)
}