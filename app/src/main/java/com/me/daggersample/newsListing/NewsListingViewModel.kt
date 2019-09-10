package com.me.daggersample.newsListing

import android.util.Log
import com.me.daggersample.base.BaseViewModel
import com.me.daggersample.common.NO_DATA
import com.me.daggersample.data.networkData.ErrorResponse
import com.me.daggersample.network.handler.NetworkHandler
import io.reactivex.disposables.Disposable

class NewsListingViewModel(val newsListingRepository: NewsListingRepository) : BaseViewModel() {

    fun getNewsListing(): Disposable {
        return NetworkHandler(this, NewsListingProcessor(newsListingRepository))
            .execute {
                if (it.newsData != null && it.newsData.size > 0) {
                    Log.e("xxx", "data : $it")
                } else {
                    handleErrorMessage.onNext(ErrorResponse(NO_DATA))
                }
            }
    }
}