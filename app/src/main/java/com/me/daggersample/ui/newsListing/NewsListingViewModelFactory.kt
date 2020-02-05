package com.me.daggersample.ui.newsListing

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewsListingViewModelFactory(
    val newsListingRepository: NewsListingRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsListingViewModel(newsListingRepository) as T
    }
}