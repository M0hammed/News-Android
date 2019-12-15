package com.me.daggersample.ui.newsListing

import android.util.Log
import com.me.daggersample.base.BaseViewModel
import com.me.daggersample.common.NO_DATA
import com.me.daggersample.data.base.ApiResponse
import com.me.daggersample.data.networkData.ErrorResponse
import com.me.daggersample.data.news.NewsModel
import com.me.daggersample.data.news.NewsResponse
import com.me.daggersample.network.handler.ResponseStatus
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class NewsListingViewModel(val newsListingRepository: NewsListingRepository) : BaseViewModel() {
    val newsListing = BehaviorSubject.create<ArrayList<NewsModel>>()

    fun getNewsListing(): Observable<ResponseStatus<ApiResponse<NewsResponse>>> {
        return newsListingRepository.getListingNews()
    }

    private fun mapNewsListing(it: ResponseStatus<ApiResponse<NewsResponse>>): ResponseStatus<ApiResponse<NewsResponse>> {
        if (it is ResponseStatus.Success) {
            val data = it.data?.result?.newsData?.get(0)
            Log.e("xxx", "title is ${data?.imageUrl}")
        }
        return it
    }

    fun validateNewsList(newsData: ArrayList<NewsModel>) {
        if (newsData != null && newsData.size > 0) {
            newsListing.onNext(newsData)
        } else {
            handleErrorMessage.onNext(ErrorResponse(NO_DATA))
        }
    }
}