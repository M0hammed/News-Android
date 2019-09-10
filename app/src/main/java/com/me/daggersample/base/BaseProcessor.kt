package com.me.daggersample.base

import com.me.daggersample.network.handler.NetworkExceptionHandler
import com.me.daggersample.network.handler.NetworkOutcome
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class BaseProcessor<ResponseType : Any> {
    fun execute(): Observable<NetworkOutcome<ResponseType>> {
        return process().doOnSubscribe { validate() }
            .onErrorReturn {
                NetworkOutcome(false, null, NetworkExceptionHandler(it).getFailureException())
            }
    }

    abstract fun validate() // to call validation
    abstract fun process(): PublishSubject<NetworkOutcome<ResponseType>> // to call apis
}