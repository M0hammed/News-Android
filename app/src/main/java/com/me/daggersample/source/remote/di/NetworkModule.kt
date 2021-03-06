package com.me.daggersample.source.remote.di

import com.me.daggersample.BuildConfig
import com.me.daggersample.source.remote.apiInterface.ConstantsKeys
import com.me.daggersample.source.remote.apiInterface.RetrofitApisInterface
import com.me.daggersample.utils.Platform
import dagger.Module
import dagger.Provides
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @Singleton
    @Provides
    internal fun provideHttpInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            if (Platform.isBuildConfigDebug())
                this.level = HttpLoggingInterceptor.Level.BODY
            else
                this.level = HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    internal fun providerRequestInterceptor(): Interceptor = Interceptor { chain ->
        val url = chain.request().url
            .newBuilder()
            .addQueryParameter(ConstantsKeys.ApiKeys.API_KEY, BuildConfig.API_KEY)
            .build()
        val request = chain.request().newBuilder().url(url).build()
        chain.proceed(request)
    }

    @Singleton
    @Provides
    internal fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        interceptor: Interceptor
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(interceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    internal fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @Named("baseUrl") baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    internal fun provideRetrofitApisInterface(retrofit: Retrofit): RetrofitApisInterface {
        return retrofit.create(RetrofitApisInterface::class.java)
    }
}
