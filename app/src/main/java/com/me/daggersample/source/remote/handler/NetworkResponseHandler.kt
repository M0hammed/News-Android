package com.me.daggersample.source.remote.handler

import com.google.gson.Gson
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.base.ErrorTypes
import com.me.daggersample.model.networkData.ErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import retrofit2.Response


private const val SUCCESS_STATUS = "ok"

fun <T> Call<T>.getNetworkResponse(retry: Long = 0): Flow<Status<T>> = flow {
    val response = this@getNetworkResponse.execute()
    val responseStatus = getResponseStatus(response)
    emit(responseStatus)
}.flowOn(Dispatchers.IO)
    .catch { cause: Throwable ->
        emit(
            Status.Error(ErrorTypes.GeneralError(cause = cause))
        )
    }

private fun <T> getResponseStatus(response: Response<T>): Status<T> {
    return if (response.isSuccessful) {
        val apiResponse = response.body() as ApiResponse<T>?
        if (apiResponse != null) {
            if (apiResponse.status == SUCCESS_STATUS) {
                Status.Success(data = response.body())
            } else {
                Status.Error(ErrorTypes.ServerError)
            }
        } else {
            Status.Error(ErrorTypes.ApiFail)
        }
    } else {
        val errorResponse = Gson().fromJson(
            response.errorBody()?.string(), ErrorResponse::class.java
        )
        Status.Error(ErrorTypes.ApiFail)
    }
}