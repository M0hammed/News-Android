package com.me.daggersample.ui.newsListing

import com.me.daggersample.base.BaseRepository
import com.me.daggersample.data.base.ApiResponse
import com.me.daggersample.data.news.NewsResponse
import com.me.daggersample.network.apiInterface.NewsListingApiInterface
import com.me.daggersample.network.handler.ResponseStatus
import com.me.daggersample.network.handler.getNetworkResponse
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class NewsListingRepository @Inject constructor(val newsApiInterface: NewsListingApiInterface) :
    BaseRepository() {

    fun getListingNews(): Observable<ResponseStatus<ApiResponse<NewsResponse>>> {

        return newsApiInterface.getNewsListing().getNetworkResponse()
    }
}