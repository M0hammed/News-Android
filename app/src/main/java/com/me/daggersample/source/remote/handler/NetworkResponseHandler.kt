package com.me.daggersample.source.remote.handler

import androidx.annotation.WorkerThread
import com.google.gson.Gson
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.networkData.ErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Response


private const val SUCCESS_STATUS = "ok"

@WorkerThread
fun <T> Call<T>.getNetworkResponse(retry: Long = 0): Flow<ResponseStatus<T>> = flow {
    try {
        val response = this@getNetworkResponse.execute()
        val responseStatus = getResponseStatus(response)
        emit(responseStatus)
    } catch (exception: Exception) {
        emit(ResponseStatus.Error())
    }

}.catch { cause: Throwable -> emit(ResponseStatus.Error(cause = cause)) }
    .flowOn(Dispatchers.IO)

private fun <T> getResponseStatus(response: Response<T>): ResponseStatus<T> {
    return if (response.isSuccessful) {
        val apiResponse = response.body() as ApiResponse<T>?
        if (apiResponse != null) {
            if (apiResponse.status == SUCCESS_STATUS) {
                ResponseStatus.Success(data = response.body())
            } else {
                ResponseStatus.ServerError(serverMessage = apiResponse.message)
            }
        } else {
            ResponseStatus.Error()
        }
    } else {
        val errorResponse = Gson().fromJson(
            response.errorBody()?.string(), ErrorResponse::class.java
        )
        ResponseStatus.ApiFailed(response.code(), errorResponse)
    }
}