package com.me.daggersample.source.remote.data_source

import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.headLine.HeadLineModel
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.handler.ResponseStatus
import kotlinx.coroutines.flow.Flow


interface IRemoteDataSource {
    fun cancelApiCall(tag: String)
    fun getNews(): Flow<ResponseStatus<ApiResponse<ArrayList<Sources>>>>
}