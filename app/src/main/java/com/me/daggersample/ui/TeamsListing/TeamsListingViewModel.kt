package com.me.daggersample.ui.TeamsListing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.me.daggersample.base.BaseViewModel
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.team.Teams
import com.me.daggersample.source.remote.handler.ResponseStatus
import io.reactivex.Completable

class TeamsListingViewModel(private val teamsListingRepository: TeamsListingRepository) :
    BaseViewModel() {
    private val _teamsListing = MutableLiveData<ArrayList<Teams>>()
    val teamsListing: LiveData<ArrayList<Teams>>
        get() = _teamsListing
    private var cashedTeamsList: ArrayList<Teams>? = null

    fun getNewsListing(): Completable {
        return teamsListingRepository.getListingTeams().map { mapTeamsListing(it) }
            .ignoreElements()
    }

    private fun mapTeamsListing(it: ResponseStatus<ApiResponse<ArrayList<Teams>>>): ResponseStatus<ApiResponse<ArrayList<Teams>>> {
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
    private fun validateTeamsListing(teamsList: ArrayList<Teams>?) {
        if (teamsList != null) {
            if (teamsList.isNotEmpty()) {// data received successfully
                _teamsListing.value = teamsList
                updateCashedTeamsList(teamsList)
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
    private fun updateCashedTeamsList(teamsList: ArrayList<Teams>) {
        this.cashedTeamsList = teamsList
    }

    fun cancelApiCall() {
        teamsListingRepository.cancelApiCall()
    }
}