package com.me.daggersample.source.remote.data_source

import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.base.Status
import com.me.daggersample.model.headLine.HeadLineModel
import com.me.daggersample.model.source.Sources
import kotlinx.coroutines.flow.Flow

interface IRemoteDataSource {
    fun cancelApiCall(tag: String)
    fun getNews(): Flow<Status<ApiResponse<ArrayList<Sources>>>>
    fun getHeadLinesList(sourceId: String): Flow<Status<ApiResponse<ArrayList<HeadLineModel>>>>
}
