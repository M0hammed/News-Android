package com.me.daggersample.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.me.daggersample.customViews.CustomProgressDialog
import com.me.daggersample.model.networkData.ErrorResponse
import com.me.daggersample.messageHandler.ErrorMessageHandler
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<V : BaseViewModel> : DaggerFragment() {

    protected lateinit var viewModel: V
    private var disposable: CompositeDisposable? = null
    private var errorMessageHandler: ErrorMessageHandler? = null
    private var progressDialog: CustomProgressDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(getLayoutResource(), container, false)

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
        progressDialog = CustomProgressDialog(requireContext())
        progressDialog?.setType(CustomProgressDialog.SPINNER_PROGRESS_DIALOG)
        progressDialog?.setCancellable(false)
        progressDialog?.setMessage(getString(getProgressMessage()))
        progressDialog?.show()
    }

    private fun hideProgressDialog() { // hide loading dialog
        progressDialog?.dismiss()
    }

    protected fun addDisposable(): CompositeDisposable? { // handle disposable
        disposable = CompositeDisposable()
        return disposable
    }

    override fun onDestroy() { // for disposing when destroy
        disposable?.dispose()
        super.onDestroy()
    }

    protected abstract fun getLayoutResource(): Int

    protected abstract fun initViews(view: View)

    protected abstract fun initViewModel()

    protected abstract fun initialize()

    protected abstract fun setListeners()

    protected abstract fun getProgressMessage(): Int

}