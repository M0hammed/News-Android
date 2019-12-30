package com.me.daggersample.ui.newsListing

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class NewsListingViewModelFactory @Inject constructor(
    val newsListingRepository: NewsListingRepository,
    val newsListingFragment: NewsListingFragment
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        Log.e("xxx", "test string ${newsListingFragment.getTestString()}")
        return NewsListingViewModel(newsListingRepository) as T
    }
}