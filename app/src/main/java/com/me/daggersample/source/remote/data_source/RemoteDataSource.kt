package com.me.daggersample.source.remote.data_source

import com.google.gson.Gson
import com.me.daggersample.model.base.ApiErrorResponse
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.base.ErrorTypes
import com.me.daggersample.model.base.Status
import com.me.daggersample.model.headLine.HeadLineModel
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.apiInterface.RetrofitApisInterface
import com.me.daggersample.source.remote.handler.NetworkStatus
import com.me.daggersample.source.remote.handler.ResponseError
import com.me.daggersample.source.remote.handler.getNetworkResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Call

class RemoteDataSource(private val retrofitApisInterface: RetrofitApisInterface) :
    IRemoteDataSource {
    private val apiCallMap = HashMap<String, Call<*>>()

    private fun <R> executeApiCall(
        call: Call<R>,
        tag: String
    ): Flow<Status<R>> {
        apiCallMap[tag] = call
        return call.getNetworkResponse().map {
            when (it) {
                is NetworkStatus.Success -> Status.Success(it.data)
                is NetworkStatus.Error -> Status.Error(mapErrorResponse(it.responseError, it.cause))
            }
        }
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
        val newsCall = retrofitApisInterface.getNews()
        return executeApiCall(newsCall, "sourcesTag")
    }

    override fun getHeadLinesList(sourceId: String): Flow<Status<ApiResponse<ArrayList<HeadLineModel>>>> {
        val headLineList = retrofitApisInterface.getHeadLineList(sourceId)
        return executeApiCall(headLineList, "headLinesTag")
    }

    private fun mapErrorResponse(
        errorResponseError: ResponseError?,
        cause: Throwable?
    ): ErrorTypes {
        return if (cause != null) {
            ErrorTypes.UnknownError(cause)
        } else {
            val apiErrorResponse = errorResponseError?.errorBody?.let {
                Gson().fromJson(it.string(), ApiErrorResponse::class.java)
            } ?: ApiErrorResponse(code = errorResponseError?.statusCode?.toString())
            ErrorTypes.ServerError(apiErrorResponse)
        }
    }
}
