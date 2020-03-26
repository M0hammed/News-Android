package com.me.daggersample.ui.SourcesListing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class SourcesListingViewModelFactory @Inject constructor(
    private val newsListingRepository: SourcesListingRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SourcesListingViewModel(newsListingRepository) as T
    }
}