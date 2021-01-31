package com.me.daggersample.ui.SourcesListing.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.me.daggersample.ui.SourcesListing.data.ISourcesListingRepository
import javax.inject.Inject

class SourcesListingViewModelFactory @Inject constructor(
    private val newsListingRepository: ISourcesListingRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SourcesListingViewModel(newsListingRepository) as T
    }
}