package com.me.daggersample.source.remote.data_source

import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.team.Teams
import com.me.daggersample.source.remote.apiInterface.RetrofitApisInterface
import com.me.daggersample.source.remote.handler.ResponseStatus
import com.me.daggersample.source.remote.handler.getNetworkResponse
import io.reactivex.Observable
import retrofit2.Call

class RemoteDataSource(private val retrofitApisInterface: RetrofitApisInterface) : IRemoteDataSource {
    private val apiCallMap = HashMap<String, Call<*>>()

    private fun <R> executeApiCall(
        call: Call<ApiResponse<R>>, tag: String
    ): Observable<ResponseStatus<ApiResponse<R>>> {
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

    override fun getTeamsList(): Observable<ResponseStatus<ApiResponse<ArrayList<Teams>>>> {
        val teamsCall = retrofitApisInterface.getTeams()
        return executeApiCall(teamsCall, "teamsTag")
    }
}