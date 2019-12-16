package com.me.daggersample.source.remote.apiClient

import com.me.daggersample.BuildConfig
import com.me.daggersample.source.remote.apiInterface.RetrofitApisInterface
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.source.remote.data_source.RemoteDataSource
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    internal fun provideHttpInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        return httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    internal fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    internal fun provideRetrofitApisInterface(retrofit: Retrofit): RetrofitApisInterface {
        return retrofit.create(RetrofitApisInterface::class.java)
    }

    @Singleton
    @Provides
    internal fun provideRemoteDataSource(retrofitApisInterface: RetrofitApisInterface): IRemoteDataSource {
        return RemoteDataSource(retrofitApisInterface)
    }
}