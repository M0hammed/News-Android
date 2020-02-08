package com.me.daggersample.ui.SourcesListing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SourcesListingViewModelFactory(
    val newsListingRepository: SourcesListingRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SourcesListingViewModel(newsListingRepository) as T
    }
}