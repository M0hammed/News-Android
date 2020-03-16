package com.me.daggersample.ui.HeadLines

import com.me.daggersample.base.BaseRepository
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.headLine.HeadLineModel
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.source.remote.handler.ResponseStatus
import com.me.daggersample.validator.INetworkValidator
import io.reactivex.Observable

class HeadLinesRepository(
    private val iRemoteDataSource: IRemoteDataSource,
    private val networkValidator: INetworkValidator
) : BaseRepository() {
    override fun cancelApiCall() {
        iRemoteDataSource.cancelApiCall("headLines")
    }

    fun getHeadLinesList(sourceId: String): Observable<ResponseStatus<ApiResponse<ArrayList<HeadLineModel>>>> {
        return if (networkValidator.isConnected())
            iRemoteDataSource.getHeadLinesList(sourceId)
        else
            Observable.just(ResponseStatus.NoNetwork() as ResponseStatus<ApiResponse<ArrayList<HeadLineModel>>>)
    }
}