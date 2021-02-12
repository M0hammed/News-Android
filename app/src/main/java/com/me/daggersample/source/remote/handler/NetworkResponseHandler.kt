package com.me.daggersample.source.remote.handler

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Response

@WorkerThread
fun <T> Call<T>.getNetworkResponse(): Flow<NetworkStatus<T>> = flow {
    val response = this@getNetworkResponse.execute()
    val responseStatus = getResponseStatus(response)
    emit(responseStatus)
}.catch { cause: Throwable ->
    cause.printStackTrace()
    emit(NetworkStatus.Error(null, cause))
}

private fun <T> getResponseStatus(response: Response<T>): NetworkStatus<T> {

    return if (response.isSuccessful && response.body() != null) {
        NetworkStatus.Success(data = response.body()!!)
    } else {
        NetworkStatus.Error(ResponseError(response.code(), response.errorBody()), null)
    }
}
