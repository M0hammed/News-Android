package com.me.daggersample.source.remote.data_source

import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.team.Teams
import com.me.daggersample.source.remote.handler.ResponseStatus
import io.reactivex.Observable


interface IRemoteDataSource {
    fun cancelApiCall(tag: String)
    fun getTeamsList(): Observable<ResponseStatus<ApiResponse<ArrayList<Teams>>>>
}