package com.me.daggersample.newsListing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class NewsListingViewModelFactory @Inject constructor(val newsListingRepository: NewsListingRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        NewsListingViewModel(newsListingRepository) as T
}