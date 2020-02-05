package com.me.daggersample.ui.TeamsListing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TeamsListingViewModelFactory(
    val newsListingRepository: TeamsListingRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TeamsListingViewModel(newsListingRepository) as T
    }
}