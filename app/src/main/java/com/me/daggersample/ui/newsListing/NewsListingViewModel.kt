package com.me.daggersample.ui.newsListing

import android.util.Log
import com.me.daggersample.base.BaseViewModel
import com.me.daggersample.common.NO_DATA
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.networkData.ErrorResponse
import com.me.daggersample.model.news.NewsModel
import com.me.daggersample.model.news.NewsResponse
import com.me.daggersample.model.team.Teams
import com.me.daggersample.source.remote.handler.ResponseStatus
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class NewsListingViewModel(val newsListingRepository: NewsListingRepository) : BaseViewModel() {
    val newsListing = BehaviorSubject.create<ArrayList<NewsModel>>()

    fun getNewsListing(): Completable {
        return newsListingRepository.getListingNews().map { mapNewsListing(it) }
            .ignoreElements()
    }

    private fun mapNewsListing(it: ResponseStatus<ApiResponse<ArrayList<Teams>>>): ResponseStatus<ApiResponse<ArrayList<Teams>>> {
        if (it is ResponseStatus.Success) {
            val data = it.data?.result?.get(0)
            Log.e("xxx", "title is ${data?.teamFlag}")
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

    fun cancelApiCall() {
        newsListingRepository.cancelApiCall()
    }
}