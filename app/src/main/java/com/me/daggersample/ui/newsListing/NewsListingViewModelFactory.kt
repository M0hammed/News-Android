package com.me.daggersample.ui.newsListing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class NewsListingViewModelFactory @Inject constructor(val newsListingRepository: NewsListingRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        NewsListingViewModel(newsListingRepository) as T
}