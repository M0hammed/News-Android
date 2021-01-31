package com.me.daggersample.ui.HeadLines.data

import com.me.daggersample.base.BaseRepository
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.base.ErrorTypes
import com.me.daggersample.model.base.Status
import com.me.daggersample.model.headLine.HeadLineModel
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.validator.INetworkValidator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.ArrayList
import javax.inject.Inject

class HeadLinesRepository @Inject constructor(
    private val iRemoteDataSource: IRemoteDataSource,
    private val networkValidator: INetworkValidator
) : BaseRepository(), IHeadLinesRepository {
    override fun cancelApiCall() {
        iRemoteDataSource.cancelApiCall("headLinesTag")
    }

    override fun getHeadLinesList(sourceId: String): Flow<Status<ApiResponse<ArrayList<HeadLineModel>>>> {
        return if (networkValidator.isConnected())
            iRemoteDataSource.getHeadLinesList(sourceId)
        else
            flowOf(Status.Error(ErrorTypes.NoNetwork))
    }
}