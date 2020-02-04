package com.me.daggersample.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.me.daggersample.messageHandler.ErrorMessageHandler
import com.me.daggersample.model.networkData.ErrorResponse
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<V : BaseViewModel> : Fragment() {

    protected lateinit var viewModel: V
    private var disposable: CompositeDisposable? = null
    private var errorMessageHandler: ErrorMessageHandler? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(getLayoutResource, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        errorMessageHandler = ErrorMessageHandler(requireContext())
        setListeners()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
        initialize()
        handleObservers()
    }

    private fun handleObservers() { // handle error messages and progress loading
        addDisposable()?.add(viewModel.handleProgress.subscribe {
            if (it) showProgressDialog()
            else hideProgressDialog()
        })

        addDisposable()?.add(viewModel.handleErrorMessage.subscribe {
            if (it != null) showErrorMessage(it)
        })
    }

    private fun showErrorMessage(errorModel: ErrorResponse) { // show error message
        val errorMessage = errorMessageHandler?.getErrorMessage(errorModel) ?: ""
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showProgressDialog() { // show loading dialog
    }

    private fun hideProgressDialog() { // hide loading dialog
    }

    protected fun addDisposable(): CompositeDisposable? { // handle disposable
        disposable = CompositeDisposable()
        return disposable
    }

    override fun onDestroy() { // for disposing when destroy
        disposable?.dispose()
        super.onDestroy()
    }

    @get:LayoutRes
    protected abstract val getLayoutResource: Int

    protected abstract fun initViews(view: View)

    protected abstract fun initViewModel()

    protected abstract fun initialize()

    protected abstract fun setListeners()
}