package com.me.daggersample.ui.SourcesListing

import com.me.daggersample.base.BaseRepository
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.source.remote.handler.ResponseStatus
import com.me.daggersample.validator.INetworkValidator
import io.reactivex.Observable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SourcesListingRepository @Inject constructor(
    private val iRemoteDataSource: IRemoteDataSource,
    private val networkValidator: INetworkValidator
) : BaseRepository() {

    /*fun getListingTeams(): Observable<ResponseStatus<ApiResponse<ArrayList<Sources>>>> {
        return if (networkValidator.isConnected())
            iRemoteDataSource.getTeamsList()
        else
            Observable.just(ResponseStatus.NoNetwork()) as Observable<ResponseStatus<ApiResponse<ArrayList<Sources>>>>
    }*/

    fun getListingTeams(): Flow<ResponseStatus<ApiResponse<ArrayList<Sources>>>> {
        return if (networkValidator.isConnected())
            iRemoteDataSource.getTeamsList()
        else
            flowOf(ResponseStatus.NoNetwork())
    }

    override fun cancelApiCall() {
        iRemoteDataSource.cancelApiCall("teamsTag")
    }
}