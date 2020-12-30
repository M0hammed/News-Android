package com.me.daggersample.source.remote.handler

import android.util.Log
import androidx.annotation.WorkerThread
import com.google.gson.Gson
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.networkData.ErrorResponse
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val SUCCESS_STATUS = "ok"

fun <T> Call<T>.getNetworkResponse(): Observable<ResponseStatus<T>> {
    val networkOutcome = PublishSubject.create<ResponseStatus<T>>()
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>?, response: Response<T>) {
            if (response.isSuccessful && response.code() == 200) {
                val apiResponse = response.body() as ApiResponse<T>?
                if (apiResponse != null) {
                    if (apiResponse.status == SUCCESS_STATUS) {
                        networkOutcome.onNext(ResponseStatus.Success(data = response.body()))
                    } else {
                        networkOutcome.onNext(ResponseStatus.ServerError(serverMessage = apiResponse.message) as ResponseStatus<T>)
                    }
                } else {
                    networkOutcome.onNext(ResponseStatus.Error() as ResponseStatus<T>)
                }
            } else {
                val errorResponse = Gson().fromJson(
                    response.errorBody()?.string(), ErrorResponse::class.java
                )
                networkOutcome.onNext(
                    ResponseStatus.ApiFailed(response.code(), errorResponse) as ResponseStatus<T>
                )
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            if (call.isCanceled) {
                Log.e("xxx", "call is canceled")
            } else {
                networkOutcome.onNext(ResponseStatus.Error() as ResponseStatus<T>)
            }
        }
    })

    return networkOutcome.hide()
}

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