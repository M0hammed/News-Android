package com.me.daggersample.ui.SourcesListing

import com.me.daggersample.base.BaseRepository
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.source.remote.handler.ResponseStatus
import com.me.daggersample.validator.INetworkValidator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SourcesListingRepository @Inject constructor(
    private val iRemoteDataSource: IRemoteDataSource,
    private val networkValidator: INetworkValidator
) : BaseRepository() {

    fun getNews(): Flow<ResponseStatus<ApiResponse<ArrayList<Sources>>>> {
        return if (networkValidator.isConnected())
            iRemoteDataSource.getNews()
        else
            flowOf(ResponseStatus.NoNetwork())
    }

    override fun cancelApiCall() {
        iRemoteDataSource.cancelApiCall("teamsTag")
    }
}