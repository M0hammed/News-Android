package com.me.daggersample.source.remote.apiClient

import com.me.daggersample.BuildConfig
import com.me.daggersample.source.remote.apiInterface.ConstantsKeys
import okhttp3.Interceptor
import okhttp3.Response


class RetrofitInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url
            .newBuilder()
            .addQueryParameter(ConstantsKeys.API_KEY, BuildConfig.API_KEY)
            .addQueryParameter(ConstantsKeys.COUNTRY, ConstantsKeys.EGYPT)
            .build()
        val request = chain.request().newBuilder().url(url).build()
        return chain.proceed(request)
    }
}