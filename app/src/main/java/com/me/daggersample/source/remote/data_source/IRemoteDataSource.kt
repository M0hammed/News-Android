package com.me.daggersample.source.remote.data_source

import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.headLine.HeadLineModel
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.handler.ResponseStatus
import io.reactivex.Observable


interface IRemoteDataSource {
    fun cancelApiCall(tag: String)
    fun getTeamsList(): Observable<ResponseStatus<ApiResponse<ArrayList<Sources>>>>
    fun getHeadLinesList(sourceId: String): Observable<ResponseStatus<ApiResponse<ArrayList<HeadLineModel>>>>
}