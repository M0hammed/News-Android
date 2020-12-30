package com.me.daggersample.ui.SourcesListing

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.me.daggersample.R
import com.me.daggersample.base.BaseViewModel
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.networkData.ErrorModel
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.handler.ResponseStatus
import io.reactivex.Completable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SourcesListingViewModel(private val sourcesListingRepository: SourcesListingRepository) :
    BaseViewModel() {
    private val _sourcesListing = MutableLiveData<ArrayList<Sources>>()
    val sourcesListing: LiveData<ArrayList<Sources>>
        get() = _sourcesListing
    private var cashedSourcesList: ArrayList<Sources>? = null
    val testingCashedSourcesList: ArrayList<Sources>?
        get() = cashedSourcesList

    /*fun getNewsListing(forceRefresh: Boolean = false, loadMore: Boolean = false): Completable {
        return if (cashedSourcesList.isNullOrEmpty() || forceRefresh)
            sourcesListingRepository.getListingTeams()
                .doOnSubscribe { handleSubscribeOn(forceRefresh, loadMore) }
                .doOnError { handleDoOnError(forceRefresh, loadMore) }
                .doOnNext { handleDoOnNext(forceRefresh, loadMore) }
                .map { mapTeamsListing(it, forceRefresh) }
                .ignoreElements()
        else {
            Completable.complete()
        }
    }*/

    fun getNewsListing(
        forceRefresh: Boolean = false,
        loadMore: Boolean = false
    ) {
        sourcesListingRepository.getListingTeams()
            .onStart { handleSubscribeOn(forceRefresh, loadMore) }
            .onCompletion { handleDoOnNext(forceRefresh, loadMore) }
            .map { mapTeamsListing(it, forceRefresh) }
            .launchIn(viewModelScope)
    }

    // handle subscribe on
    private fun handleSubscribeOn(forceRefresh: Boolean, loadMore: Boolean) {
        handleProgressVisibility(VISIBLE, forceRefresh, loadMore)
        _errorLayoutVisibility.value = ErrorModel(visibility = GONE)
    }

    // handle do on error
    private fun handleDoOnError(forceRefresh: Boolean, loadMore: Boolean) {// need to handle throws
        handleProgressVisibility(GONE, forceRefresh, loadMore)
        validateCashedData(ErrorModel(visibility = VISIBLE))
    }

    // handle on complete
    private fun handleDoOnNext(forceRefresh: Boolean, loadMore: Boolean) {
        handleProgressVisibility(GONE, forceRefresh, loadMore)
    }

    private fun mapTeamsListing(
        it: ResponseStatus<ApiResponse<ArrayList<Sources>>>, forceRefresh: Boolean
    ): ResponseStatus<ApiResponse<ArrayList<Sources>>> {
        when (it) {
            is ResponseStatus.Success -> {
                validateTeamsListing(it.data?.result, forceRefresh)
            }
            is ResponseStatus.NoNetwork -> {
                validateCashedData(
                    ErrorModel(
                        message = it.message,
                        subMessage = it.subMessage,
                        errorIcon = R.drawable.like,
                        visibility = VISIBLE
                    )
                )
            }
            is ResponseStatus.ServerError -> {
                validateCashedData(
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
                validateCashedData(
                    ErrorModel(
                        serverMessage = it.errorResponse.message,
                        subMessage = R.string.please_try_again,
                        errorIcon = R.drawable.like,
                        visibility = VISIBLE
                    )
                )
            }
            is ResponseStatus.Error -> {
                validateCashedData(
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
            updateCashedTeamsList(sourcesList, forceRefresh)
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
                _errorMessage.value =
                    ErrorModel(message = R.string.something_went_wrong) // error message
            }
        }
    }

    // check cashed data if should show error layout or show toast
    private fun validateCashedData(errorModel: ErrorModel) {
        if (cashedSourcesList.isNullOrEmpty()) {// show error model
            _errorLayoutVisibility.value = errorModel
        } else {
            _errorMessage.value = errorModel
        }
    }

    // update cashed data with given new values
    private fun updateCashedTeamsList(
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

    override fun onCleared() {
        super.onCleared()
        sourcesListingRepository.cancelApiCall()
    }
}