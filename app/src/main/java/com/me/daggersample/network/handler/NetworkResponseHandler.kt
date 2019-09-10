package com.me.daggersample.network.handler

import com.me.daggersample.data.networkData.ErrorResponse
import io.reactivex.subjects.PublishSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.getNetworkResponse(): PublishSubject<NetworkOutcome<T>> {
    val networkOutcome = PublishSubject.create<NetworkOutcome<T>>()
    networkOutcome.onNext((NetworkOutcome(false, null, ErrorResponse())))
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>?, response: Response<T>) {
            if (response.isSuccessful) {
                networkOutcome.onNext(NetworkOutcome(true, response.body(), ErrorResponse()))
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            networkOutcome.onNext(NetworkOutcome(false, null, NetworkExceptionHandler(t).getFailureException()))
        }
    })

    return networkOutcome
}