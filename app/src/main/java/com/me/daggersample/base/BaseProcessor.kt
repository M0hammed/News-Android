package com.me.daggersample.base

import android.util.Log
import com.me.daggersample.network.handler.NetworkExceptionHandler
import com.me.daggersample.network.handler.NetworkOutcome
import com.me.daggersample.validator.ResponseErrorException
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class BaseProcessor<ResponseType : Any> {
    fun execute(): Observable<NetworkOutcome<ResponseType>> {
        return process().doOnSubscribe { validate() }
            .onErrorReturn {
                Log.e("xxx", "onErrorReturn : $it")
                val exception = it as ResponseErrorException
                NetworkOutcome(false, null, exception.errorModel)
            }
    }

    abstract fun validate() // to call validation
    abstract fun process(): PublishSubject<NetworkOutcome<ResponseType>> // to call apis
}