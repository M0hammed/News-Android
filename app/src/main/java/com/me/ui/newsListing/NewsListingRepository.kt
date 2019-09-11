package com.me.ui.newsListing

import com.me.daggersample.base.BaseRepository
import com.me.daggersample.data.NewsResponse
import com.me.daggersample.network.apiInterface.NewsListingApiInterface
import com.me.daggersample.network.handler.NetworkOutcome
import com.me.daggersample.network.handler.getNetworkResponse
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class NewsListingRepository @Inject constructor(val newsApiInterface: NewsListingApiInterface) :
    BaseRepository() {

    fun getListingNews(): PublishSubject<NetworkOutcome<NewsResponse>> {

        return newsApiInterface.getNewsListing().getNetworkResponse()
    }
}