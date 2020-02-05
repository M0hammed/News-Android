package com.me.daggersample.ui.TeamsListing

import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.validator.INetworkValidator
import dagger.Module
import dagger.Provides

@Module
class TeamsListingModule {

    @Provides
    internal fun providesNewsListingRepository(
        iRemoteDataSource: IRemoteDataSource, networkValidator: INetworkValidator
    ): TeamsListingRepository =
        TeamsListingRepository(iRemoteDataSource, networkValidator)


    @Provides
    internal fun providesNewsListingViewModelProvider(
        newsListingRepository: TeamsListingRepository
    ) = TeamsListingViewModelFactory(newsListingRepository)
}