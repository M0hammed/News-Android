package com.me.daggersample.source.remote.handler

import android.util.Log
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.source.remote.handler.networkStatusCodes.SUCCESS_STATUS
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                        networkOutcome.onNext(ResponseStatus.ServerError() as ResponseStatus<T>)
                    }
                } else {
                    networkOutcome.onNext(ResponseStatus.ApiFailed(response.code()) as ResponseStatus<T>)
                }
            } else {
                networkOutcome.onNext(ResponseStatus.Error() as ResponseStatus<T>)
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

object networkStatusCodes {
    const val SUCCESS_STATUS = "ok"
}