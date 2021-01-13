package com.me.daggersample.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.me.daggersample.extentions.makeErrorMessage
import com.me.daggersample.model.base.Progress
import com.me.daggersample.model.networkData.ErrorModel
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.error_layout.view.*

abstract class BaseFragment<V : BaseViewModel> : Fragment() {

    protected lateinit var viewModel: V

    override fun onAttach(context: Context) {
        super.onAttach(context)
        initDependencyInjection()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(getLayoutResource, container, false)

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
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast(requireContext()).makeErrorMessage(
                requireContext(), it?.serverMessage ?: getString(it.message)
            )
        }
    }

    protected fun handleProgressVisibility(progress: Progress, progressView: View) {
        when (progressView) {
            is ProgressBar -> if (progress.isLoading) progressView.visibility =
                    View.VISIBLE else progressView.visibility = View.GONE
            is SwipeRefreshLayout ->
                progressView.isRefreshing = progress.isLoading
        }
    }

    protected fun bindErrorLayout(errorLayout: View, errorModel: ErrorModel) {
        errorLayout.visibility = errorModel.visibility
        errorLayout.tvErrorMessage.text =
            errorModel.serverMessage ?: getString(errorModel.message)
        errorLayout.tvErrorSubMessage.text = getString(errorModel.subMessage)
        imgErrorIcon.setImageResource(errorModel.errorIcon)
    }

    @get:LayoutRes
    protected abstract val getLayoutResource: Int

    protected abstract fun initViews(view: View)

    protected abstract fun initDependencyInjection()

    protected abstract fun initViewModel()

    protected abstract fun initialize()

    protected abstract fun setListeners()
}