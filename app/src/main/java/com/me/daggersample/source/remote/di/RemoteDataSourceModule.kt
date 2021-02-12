package com.me.daggersample.source.remote.di

import android.content.Context
import com.me.daggersample.source.remote.apiInterface.RetrofitApisInterface
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.source.remote.data_source.RemoteDataSource
import com.me.daggersample.validator.INetworkValidator
import com.me.daggersample.validator.NetworkValidator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RemoteDataSourceModule {
    @Singleton
    @Provides
    internal fun provideRemoteDataSource(retrofitApisInterface: RetrofitApisInterface): IRemoteDataSource {
        return RemoteDataSource(retrofitApisInterface)
    }

    @Singleton
    @Provides
    internal fun provideNetworkValidator(context: Context): INetworkValidator =
        NetworkValidator(context)
}
