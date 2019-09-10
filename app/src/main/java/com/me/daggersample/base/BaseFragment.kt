package com.me.daggersample.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<V : BaseViewModel> : DaggerFragment() {

    protected lateinit var viewModel: V
    private var disposable: CompositeDisposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(getLayoutResource(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setListeners()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
        initialize()
        handleObservers()
    }

    private fun handleObservers() {
        addDisposable()?.add(viewModel.handleProgress.subscribe {
            Log.e("xxx", "handle progress : $it")
        })


    }

    protected fun addDisposable(): CompositeDisposable? {
        disposable = CompositeDisposable()
        return disposable
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }

    protected abstract fun getLayoutResource(): Int

    protected abstract fun initViews(view: View)

    protected abstract fun initViewModel()

    protected abstract fun initialize()

    protected abstract fun setListeners()


}