package com.me.daggersample.ui.SourcesListing

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.viewModelScope
import com.me.daggersample.R
import com.me.daggersample.base.BaseViewModel
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.base.ErrorTypes
import com.me.daggersample.model.networkData.ErrorModel
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.handler.Status
import kotlinx.coroutines.flow.*

class SourcesListingViewModel(private val sourcesListingRepository: SourcesListingRepository) :
    BaseViewModel() {

    private val _sourcesListingState = MutableStateFlow<Status<ArrayList<Sources>>>(Status.Idle)
    val sourcesListingState: StateFlow<Status<ArrayList<Sources>>>
        get() = _sourcesListingState


    private var cashedSourcesList: ArrayList<Sources>? = null

    fun refreshNewsListing() {
        getNewsListing(true, loadMore = false)
    }

    fun getNewsListing() {
        getNewsListing(forceRefresh = false, loadMore = false)
    }

    private fun getNewsListing(
        forceRefresh: Boolean = false,
        loadMore: Boolean = false
    ) {
        if (cashedSourcesList.isNullOrEmpty() || forceRefresh)
            sourcesListingRepository.getNews()
                .onStart { doOnStart(forceRefresh, loadMore) }
                .onCompletion { doOnCompletion(forceRefresh, loadMore) }
                .map { mapTeamsListing(it, forceRefresh) }
                .onEach { emitStatus(it) }
                .catch { doOnError(forceRefresh, loadMore) }
                .launchIn(viewModelScope)
    }

    // show progress and hide error layout
    private suspend fun doOnStart(forceRefresh: Boolean, loadMore: Boolean) {
        handleProgressVisibility(true, forceRefresh, loadMore)
        _errorLayoutVisibility.value = ErrorModel(visibility = GONE)
    }

    // hide progress layout
    private suspend fun doOnCompletion(forceRefresh: Boolean, loadMore: Boolean) {
        handleProgressVisibility(false, forceRefresh, loadMore)
    }

    // hide progress layout and validate if should show error layout
    private suspend fun doOnError(forceRefresh: Boolean, loadMore: Boolean) {
        handleProgressVisibility(false, forceRefresh, loadMore)
        validateCachedData(ErrorModel(visibility = VISIBLE))
    }

    private suspend fun mapTeamsListing(
        it: Status<ApiResponse<ArrayList<Sources>>>, forceRefresh: Boolean
    ): Status<ArrayList<Sources>> {
        return when (it) {
            is Status.Success -> validateSourcesList(it.data?.result, forceRefresh)
            is Status.Idle -> Status.Idle
            else -> validateCachedData(ErrorModel())
        }
    }

    // validate list Size and nullability
    private suspend fun validateSourcesList(
        sourcesList: ArrayList<Sources>?, forceRefresh: Boolean
    ): Status<ArrayList<Sources>> {
        return if (!sourcesList.isNullOrEmpty()) {
            updateCachedNewsList(sourcesList, forceRefresh)
            Status.Success(sourcesList)
        } else {
            if (cashedSourcesList.isNullOrEmpty()) {
                Status.Error(ErrorTypes.NoData)
            } else {
                emitMessage(ErrorModel(message = R.string.something_went_wrong))
                Status.Idle
            }
        }
    }

    // check cashed data if should show error layout or show toast
    private suspend fun validateCachedData(errorModel: ErrorModel): Status<ArrayList<Sources>> {
        return if (cashedSourcesList.isNullOrEmpty()) {
            Status.Error(ErrorTypes.NoData)
        } else {
            emitMessage(errorModel)
            Status.Idle
        }
    }

    // update cashed data with given new values
    private fun updateCachedNewsList(
        sourcesList: ArrayList<Sources>, forceRefresh: Boolean
    ) {
        when {
            cashedSourcesList == null -> this.cashedSourcesList = sourcesList
            forceRefresh -> {
                this.cashedSourcesList?.clear()
                this.cashedSourcesList?.addAll(sourcesList)
            }
            else -> {
                this.cashedSourcesList?.addAll(sourcesList)
            }
        }
    }

    private fun emitStatus(sourceStatus: Status<ArrayList<Sources>>) {
        _sourcesListingState.value = sourceStatus
    }
}