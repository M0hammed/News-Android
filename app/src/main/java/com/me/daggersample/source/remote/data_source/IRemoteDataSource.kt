package com.me.daggersample.source.remote.data_source

import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.source.Sources
import com.me.daggersample.model.base.Status
import kotlinx.coroutines.flow.Flow


interface IRemoteDataSource {
    fun cancelApiCall(tag: String)
    fun getNews(): Flow<Status<ApiResponse<ArrayList<Sources>>>>
}