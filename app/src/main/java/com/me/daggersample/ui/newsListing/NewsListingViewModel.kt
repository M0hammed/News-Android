package com.me.daggersample.ui.newsListing

import com.me.daggersample.base.BaseViewModel
import com.me.daggersample.common.NO_DATA
import com.me.daggersample.data.NewsModel
import com.me.daggersample.data.networkData.ErrorResponse
import com.me.daggersample.network.handler.NetworkHandler
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class NewsListingViewModel(val newsListingRepository: NewsListingRepository) : BaseViewModel() {
    val newsListing = PublishSubject.create<ArrayList<NewsModel>>()
    fun getNewsListing(): Disposable {
        return NetworkHandler(this, NewsListingProcessor(newsListingRepository))
            .execute {
                if (it.newsData != null && it.newsData.size > 0) {
                    newsListing.onNext(it.newsData)
                } else {
                    handleErrorMessage.onNext(ErrorResponse(NO_DATA))
                }
            }
    }
}