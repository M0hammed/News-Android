package com.me.daggersample.source.remote.handler

import androidx.annotation.WorkerThread
import com.me.daggersample.model.base.ErrorTypes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Response

@WorkerThread
fun <T> Call<T>.getNetworkResponse(retry: Long = 1): Flow<Status<T>> = flow {
    val response = this@getNetworkResponse.execute()
    val responseStatus = getResponseStatus(response)
    emit(responseStatus)
}.catch { cause: Throwable ->
    cause.printStackTrace()
    emit(Status.Error(ErrorTypes.GeneralError(cause = cause)))
}

private fun <T> getResponseStatus(response: Response<T>): Status<T> {

    return if (response.isSuccessful && response.body() != null) {
        Status.Success(data = response.body())
    } else {
        Status.Error(ErrorTypes.ApiFail)
    }
}