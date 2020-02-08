package com.me.daggersample.ui.SourcesListing

import android.content.Context
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.validator.INetworkValidator
import dagger.Module
import dagger.Provides

@Module
class SourcesListingModule {

    @Provides
    internal fun providesNewsListingRepository(
        iRemoteDataSource: IRemoteDataSource, networkValidator: INetworkValidator
    ): SourcesListingRepository =
        SourcesListingRepository(iRemoteDataSource, networkValidator)


    @Provides
    internal fun providesNewsListingViewModelProvider(
        newsListingRepository: SourcesListingRepository
    ) = SourcesListingViewModelFactory(newsListingRepository)

    @Provides
    internal fun provideTeamsListingAdapter(
        context: Context, sourcesListingFragment: SourcesListingFragment
    ) = SourcesListingAdapter(context, sourcesListingFragment)
}