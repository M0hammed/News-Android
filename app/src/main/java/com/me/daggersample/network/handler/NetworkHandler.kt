package com.me.daggersample.network.handler

import com.me.daggersample.base.BaseProcessor
import com.me.daggersample.base.BaseViewModel
import io.reactivex.disposables.Disposable

class NetworkHandler<T : BaseViewModel, R : Any>(private val viewModel: T, private val processor: BaseProcessor<R>) {

    fun execute(execBlock: (response: R) -> Unit): Disposable {
        return processor.execute().doOnSubscribe { viewModel.handleProgress.onNext(true) }
            .doOnError { viewModel.handleProgress.onNext(false) }
            .doOnNext { viewModel.handleProgress.onNext(false) }
            .subscribe {
                if (it.isSuccess) {
                    it.body?.apply { execBlock(this) }
                } else {
                    viewModel.handleErrorMessage.onNext(it.responseError)
                }
            }
    }
}