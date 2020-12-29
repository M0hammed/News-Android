package com.me.daggersample.source.remote.apiClient

import android.content.Context
import com.me.daggersample.BuildConfig
import com.me.daggersample.source.remote.apiInterface.RetrofitApisInterface
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.source.remote.data_source.RemoteDataSource
import com.me.daggersample.validator.INetworkValidator
import com.me.daggersample.validator.NetworkValidator
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.*

@Module
class NetworkModule {

    @Singleton
    @Provides
    internal fun provideHttpInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        return httpLoggingInterceptor.apply {
            if (BuildConfig.DEBUG)
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            else
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    internal fun providerRequestInterceptor(): Interceptor = RetrofitInterceptor()

    @Singleton
    @Provides
    internal fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor, interceptor: Interceptor
    ): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts: Array<TrustManager> = arrayOf(object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            if (trustAllCerts.isNotEmpty() && trustAllCerts.first() is X509TrustManager) {
                okHttpClient.sslSocketFactory(
                    sslSocketFactory,
                    trustAllCerts.first() as X509TrustManager
                )
                okHttpClient.hostnameVerifier(object : HostnameVerifier {
                    override fun verify(p0: String?, p1: SSLSession?): Boolean {
                        return true
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return okHttpClient
            .addInterceptor(loggingInterceptor)
            .addInterceptor(interceptor)
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

    @Singleton
    @Provides
    internal fun provideNetworkValidator(context: Context): INetworkValidator =
        NetworkValidator(context)
}