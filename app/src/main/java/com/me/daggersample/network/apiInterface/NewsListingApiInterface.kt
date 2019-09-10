package com.me.daggersample.network.apiInterface

import com.me.daggersample.data.NewsResponse
import retrofit2.Call
import retrofit2.http.GET

interface NewsListingApiInterface {
    @GET("GetNews")
    fun getNewsListing(): Call<NewsResponse>
}