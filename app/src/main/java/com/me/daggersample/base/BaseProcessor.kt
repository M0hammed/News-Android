package com.me.daggersample.base

import com.me.daggersample.network.handler.NetworkOutcome
import com.me.daggersample.network.handler.ResponseErrorException
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class BaseProcessor<ResponseType : Any> {
    fun execute(): Observable<NetworkOutcome<ResponseType>> {
        return try {
            validate()
            process()
        } catch (ex: ResponseErrorException) {
            Observable.just(NetworkOutcome<ResponseType>(false, null, ex.errorModel))
                .onErrorReturn {
                    val exception = it as ResponseErrorException
                    NetworkOutcome(false, null, exception.errorModel)
                }
        }
    }

    abstract fun validate() // to call validation
    abstract fun process(): PublishSubject<NetworkOutcome<ResponseType>> // to call apis
}