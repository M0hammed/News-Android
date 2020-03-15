package com.me.daggersample.ui.HeadLines

import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.validator.INetworkValidator
import dagger.Module
import dagger.Provides

@Module
class HeadLinesModule {
    @Provides
    internal fun providesHeadLinesRepository(
        iRemoteDataSource: IRemoteDataSource, validator: INetworkValidator
    ): HeadLinesRepository {
        return HeadLinesRepository(iRemoteDataSource, validator)
    }

    @Provides
    internal fun providesHeadLinesViewModelFactory(headLinesRepository: HeadLinesRepository): HeadLinesViewModelFactory {
        return HeadLinesViewModelFactory(headLinesRepository)
    }
}