package com.me.daggersample.ui.SourcesListing

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.viewModelScope
import com.me.daggersample.R
import com.me.daggersample.base.BaseViewModel
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.networkData.ErrorModel
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.handler.ResponseStatus
import kotlinx.coroutines.flow.*

class SourcesListingViewModel(private val sourcesListingRepository: SourcesListingRepository) :
    BaseViewModel() {
    private val _sourcesListing = MutableStateFlow<ArrayList<Sources>?>(null)
    val sourcesListing: StateFlow<ArrayList<Sources>?>
        get() = _sourcesListing
    private var cashedSourcesList: ArrayList<Sources>? = null
    val testingCashedSourcesList: ArrayList<Sources>?
        get() = cashedSourcesList

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
                .catch { doOnError(forceRefresh, loadMore) }
                .launchIn(viewModelScope)
    }

    // show progress and hide error layout
    private fun doOnStart(forceRefresh: Boolean, loadMore: Boolean) {
        handleProgressVisibility(true, forceRefresh, loadMore)
        _errorLayoutVisibility.value = ErrorModel(visibility = GONE)
    }

    // hide progress layout
    private fun doOnCompletion(forceRefresh: Boolean, loadMore: Boolean) {
        handleProgressVisibility(false, forceRefresh, loadMore)
    }

    // hide progress layout and validate if should show error layout
    private fun doOnError(forceRefresh: Boolean, loadMore: Boolean) {// need to handle throws
        handleProgressVisibility(false, forceRefresh, loadMore)
        validateCachedData(ErrorModel(visibility = VISIBLE))
    }

    private fun mapTeamsListing(
        it: ResponseStatus<ApiResponse<ArrayList<Sources>>>, forceRefresh: Boolean
    ): ResponseStatus<ApiResponse<ArrayList<Sources>>> {
        when (it) {
            is ResponseStatus.Success -> {
                validateTeamsListing(it.data?.result, forceRefresh)
            }
            is ResponseStatus.NoNetwork -> {
                validateCachedData(
                    ErrorModel(
                        message = it.message,
                        subMessage = it.subMessage,
                        errorIcon = R.drawable.like,
                        visibility = VISIBLE
                    )
                )
            }
            is ResponseStatus.ServerError -> {
                validateCachedData(
                    ErrorModel(
                        serverMessage = it.serverMessage,
                        message = R.string.something_went_wrong,
                        subMessage = R.string.please_try_again,
                        errorIcon = R.drawable.like,
                        visibility = VISIBLE
                    )
                )
            }
            is ResponseStatus.ApiFailed -> {
                // handle api failure
                validateCachedData(
                    ErrorModel(
                        serverMessage = it.errorResponse.message,
                        subMessage = R.string.please_try_again,
                        errorIcon = R.drawable.like,
                        visibility = VISIBLE
                    )
                )
            }
            is ResponseStatus.Error -> {
                validateCachedData(
                    ErrorModel(
                        message = R.string.something_went_wrong,
                        subMessage = R.string.please_try_again,
                        errorIcon = R.drawable.like,
                        visibility = VISIBLE
                    )
                )
            }
        }
        return it
    }

    // validate list Size and nullability
    private fun validateTeamsListing(
        sourcesList: ArrayList<Sources>?, forceRefresh: Boolean
    ) {
        if (sourcesList != null) {
            // update cashed list with remote data with size or empty
            updateCachedNewsList(sourcesList, forceRefresh)
            if (sourcesList.isNotEmpty()) {
                _errorLayoutVisibility.value = ErrorModel(visibility = GONE)
            } else {
                _errorLayoutVisibility.value =
                    ErrorModel(
                        message = R.string.no_data,
                        subMessage = R.string.please_try_again,
                        errorIcon = R.drawable.like,
                        visibility = VISIBLE
                    )
            }
            _sourcesListing.value = cashedSourcesList
        } else {
            /*data error
             *so check cashed data if null or empty fire error layout
             *else fire error message*/
            if (cashedSourcesList.isNullOrEmpty()) {
                // error layout
                _errorLayoutVisibility.value =
                    ErrorModel(
                        message = R.string.something_went_wrong,
                        subMessage = R.string.please_try_again,
                        errorIcon = R.drawable.like,
                        visibility = VISIBLE
                    )
            } else {
                _sourcesListing.value = cashedSourcesList // update list with cashed data
                _messageState.tryEmit(
                    ErrorModel(message = R.string.something_went_wrong)
                )
            }
        }
    }

    // check cashed data if should show error layout or show toast
    private fun validateCachedData(errorModel: ErrorModel) {
        if (cashedSourcesList.isNullOrEmpty()) {// show error model
            _errorLayoutVisibility.value = errorModel
        } else {
            _messageState.tryEmit(errorModel)
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
}