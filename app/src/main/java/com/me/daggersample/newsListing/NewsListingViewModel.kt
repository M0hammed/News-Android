package com.me.daggersample.newsListing

import android.util.Log
import com.me.daggersample.base.BaseViewModel
import com.me.daggersample.network.handler.NetworkHandler
import io.reactivex.disposables.Disposable

class NewsListingViewModel(val newsListingRepository: NewsListingRepository) : BaseViewModel() {

    fun getNewsListing(): Disposable {
        return NetworkHandler(this, NewsListingProcessor(newsListingRepository))
            .execute {
                Log.e("xxx", "data : " + it.newsData[0].likes)
            }
    }
}