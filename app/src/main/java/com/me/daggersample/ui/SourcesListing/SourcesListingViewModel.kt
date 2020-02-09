package com.me.daggersample.ui.SourcesListing

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.me.daggersample.R
import com.me.daggersample.base.BaseViewModel
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.networkData.ErrorModel
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.handler.ResponseStatus
import io.reactivex.Completable
import io.reactivex.Single

class SourcesListingViewModel(private val sourcesListingRepository: SourcesListingRepository) :
    BaseViewModel() {
    private val _sourcesListing = MutableLiveData<ArrayList<Sources>>()
    val sourcesListing: LiveData<ArrayList<Sources>>
        get() = _sourcesListing
    private var cashedSourcesList: ArrayList<Sources>? = null

    fun getNewsListing(forceRefresh: Boolean = false): Completable {
        return if (cashedSourcesList.isNullOrEmpty() || forceRefresh)
            sourcesListingRepository.getListingTeams()
                .map { mapTeamsListing(it, forceRefresh) }
                .ignoreElements()
        else
            Single.just(cashedSourcesList).ignoreElement()
    }

    private fun mapTeamsListing(
        it: ResponseStatus<ApiResponse<ArrayList<Sources>>>, forceRefresh: Boolean
    ): ResponseStatus<ApiResponse<ArrayList<Sources>>> {
        when (it) {
            is ResponseStatus.Success -> {
                validateTeamsListing(it.data?.result)
            }
            is ResponseStatus.NoNetwork -> {
                validateCashedData(
                    ErrorModel(
                        message = it.message,
                        subMessage = it.subMessage,
                        errorIcon = R.drawable.like
                    )
                )
            }
            is ResponseStatus.ServerError -> {
                validateCashedData(
                    ErrorModel(
                        serverMessage = it.serverMessage,
                        message = R.string.something_went_wrong,
                        subMessage = R.string.please_try_again,
                        errorIcon = R.drawable.like
                    )
                )
            }
            is ResponseStatus.ApiFailed -> {
                // handle api failure
                validateCashedData(
                    ErrorModel(
                        serverMessage = it.errorResponse.message,
                        subMessage = R.string.please_try_again,
                        errorIcon = R.drawable.like
                    )
                )
            }
            is ResponseStatus.Error -> {
                validateCashedData(
                    ErrorModel(
                        message = R.string.something_went_wrong,
                        subMessage = R.string.please_try_again,
                        errorIcon = R.drawable.like
                    )
                )
            }
        }
        return it
    }

    // validate list Size and nullability
    private fun validateTeamsListing(sourcesList: ArrayList<Sources>?) {
        if (sourcesList != null) {
            if (sourcesList.isNotEmpty()) {// data received successfully
                updateCashedTeamsList(sourcesList)
                _errorLayoutVisibility.value = ErrorModel(visibility = GONE)
            } else {// no data found
                updateCashedTeamsList(ArrayList())
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
            errorModel.visibility = VISIBLE
            _errorLayoutVisibility.postValue(errorModel)
        } else {
            _errorMessage.postValue(errorModel)
        }
    }

    // update cashed data with given new values
    private fun updateCashedTeamsList(sourcesList: ArrayList<Sources>) {
        if (cashedSourcesList == null)
            this.cashedSourcesList = sourcesList
        else
            this.cashedSourcesList?.addAll(sourcesList)
    }

    override fun onCleared() {
        super.onCleared()
        sourcesListingRepository.cancelApiCall()
    }
}