package com.me.daggersample.network.handler

import android.util.Log
import com.me.daggersample.data.networkData.ErrorResponse
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception

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
            Log.e("xxx", "failure : $t")
            networkOutcome.onNext(NetworkOutcome(false, null, NetworkExceptionHandler(t).getFailureException()))
        }
    })

    return networkOutcome
}


//fun <T> Call<T>.getNetworkResponses(): Observable<NetworkOutcome<T>> {
//    try {
//        if (execute().isSuccessful) {
//            return Observable.just(NetworkOutcome<T>(true, execute().body(), ErrorResponse()))
//        } else {
//            return Observable.just(NetworkOutcome<T>(true, null, ErrorResponse()))
//        }
//    } catch (ex: Exception) {
//        return Observable.just(NetworkOutcome<T>(false, null, NetworkExceptionHandler(ex).getFailureException()))
//    }
//}