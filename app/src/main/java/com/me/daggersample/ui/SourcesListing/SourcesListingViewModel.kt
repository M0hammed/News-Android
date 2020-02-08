package com.me.daggersample.ui.SourcesListing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.me.daggersample.base.BaseViewModel
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.handler.ResponseStatus
import io.reactivex.Completable

class SourcesListingViewModel(private val sourcesListingRepository: SourcesListingRepository) :
    BaseViewModel() {
    private val _teamsListing = MutableLiveData<ArrayList<Sources>>()
    val sourcesListing: LiveData<ArrayList<Sources>>
        get() = _teamsListing
    private var cashedSourcesList: ArrayList<Sources>? = null

    fun getNewsListing(): Completable {
        return sourcesListingRepository.getListingTeams().map { mapTeamsListing(it) }
            .ignoreElements()
    }

    private fun mapTeamsListing(it: ResponseStatus<ApiResponse<ArrayList<Sources>>>): ResponseStatus<ApiResponse<ArrayList<Sources>>> {
        when (it) {
            is ResponseStatus.Success -> {
                validateTeamsListing(it.data?.result)
            }
            is ResponseStatus.NoNetwork -> {
            }
            is ResponseStatus.ServerError -> {
            }
            is ResponseStatus.ApiFailed -> {
            }
        }
        return it
    }

    // validate list Size and nullability
    private fun validateTeamsListing(sourcesList: ArrayList<Sources>?) {
        if (sourcesList != null) {
            if (sourcesList.isNotEmpty()) {// data received successfully
                _teamsListing.value = sourcesList
                updateCashedTeamsList(sourcesList)
            } else {// no data found
                updateCashedTeamsList(ArrayList())
            }
        } else {
            /*data error
             *so check cashed data if null or empty fire error layout
             *else fire error message*/
        }
    }

    // update cashed data with given new values
    private fun updateCashedTeamsList(sourcesList: ArrayList<Sources>) {
        this.cashedSourcesList = sourcesList
    }

    fun cancelApiCall() {
        sourcesListingRepository.cancelApiCall()
    }
}