package com.me.daggersample.newsListing

import android.util.Log
import com.me.daggersample.base.BaseProcessor
import com.me.daggersample.data.NewsResponse
import com.me.daggersample.network.handler.NetworkOutcome
import io.reactivex.subjects.PublishSubject

class NewsListingProcessor(val newsListingRepository: NewsListingRepository) : BaseProcessor<NewsResponse>() {
    override fun validate() {
        Log.e("xxx", "make a validation")
    }

    override fun process(): PublishSubject<NetworkOutcome<NewsResponse>> {
        return newsListingRepository.getListingNews()
    }
}