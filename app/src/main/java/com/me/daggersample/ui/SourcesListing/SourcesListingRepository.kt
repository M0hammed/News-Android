package com.me.daggersample.ui.SourcesListing

import com.me.daggersample.base.BaseRepository
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.base.ErrorTypes
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.model.base.Status
import com.me.daggersample.validator.INetworkValidator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SourcesListingRepository @Inject constructor(
    private val iRemoteDataSource: IRemoteDataSource,
    private val networkValidator: INetworkValidator
) : BaseRepository(), ISourcesListingRepository {

    override fun printMessage() {
        println("Production Repository")
    }

    override fun getNews(): Flow<Status<ApiResponse<ArrayList<Sources>>>> {
        return if (networkValidator.isConnected())
            iRemoteDataSource.getNews()
        else
            flowOf(Status.Error(ErrorTypes.NoNetwork))
    }

    override fun cancelApiCall() {
        iRemoteDataSource.cancelApiCall("teamsTag")
    }
}