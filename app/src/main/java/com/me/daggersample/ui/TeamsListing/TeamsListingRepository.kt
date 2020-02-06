package com.me.daggersample.ui.TeamsListing

import com.me.daggersample.base.BaseRepository
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.team.Teams
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.source.remote.handler.ResponseStatus
import com.me.daggersample.validator.INetworkValidator
import io.reactivex.Observable

class TeamsListingRepository (
    private val iRemoteDataSource: IRemoteDataSource,
    private val networkValidator: INetworkValidator
) : BaseRepository() {

    fun getListingTeams(): Observable<ResponseStatus<ApiResponse<ArrayList<Teams>>>> {
        return if (networkValidator.isConnected())
            iRemoteDataSource.getTeamsList()
        else
            Observable.just(ResponseStatus.NoNetwork()) as Observable<ResponseStatus<ApiResponse<ArrayList<Teams>>>>
    }

    override fun cancelApiCall() {
        iRemoteDataSource.cancelApiCall("teamsTag")
    }
}