package com.me.daggersample.source.remote.data_source

import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.apiInterface.RetrofitApisInterface
import com.me.daggersample.source.remote.handler.Status
import com.me.daggersample.source.remote.handler.getNetworkResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class RemoteDataSource(private val retrofitApisInterface: RetrofitApisInterface) :
    IRemoteDataSource {
    private val apiCallMap = HashMap<String, Call<*>>()

    private fun <R> executeApiCall(
        call: Call<ApiResponse<R>>, tag: String
    ): Flow<Status<ApiResponse<R>>> {
        apiCallMap[tag] = call
        return call.getNetworkResponse()
    }

    override fun cancelApiCall(tag: String) {
        val call = apiCallMap[tag]
        if (call != null) {
            if (call.isCanceled) {
                apiCallMap.remove(tag)
            } else {
                call.cancel()
                apiCallMap.remove(tag)
            }
        }
    }

    override fun getNews(): Flow<Status<ApiResponse<ArrayList<Sources>>>> {
        val teamsCall = retrofitApisInterface.getNews()
        return executeApiCall(teamsCall, "teamsTag")
    }
}