package com.me.daggersample.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.me.daggersample.model.base.Progress
import com.me.daggersample.model.base.ErrorModel
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.error_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

abstract class BaseFragment : Fragment() {

    private lateinit var stateJob: Job
    protected lateinit var uiStateScope: CoroutineScope

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
    }

    override fun onStart() {
        super.onStart()
        initStateScope()
        initialize()
    }

    override fun onStop() {
        super.onStop()
        stateJob.cancel()
    }

    protected fun handleProgressVisibility(progress: Progress, progressView: View) {
        when (progressView) {
            is ProgressBar -> if (progress.isLoading) progressView.visibility =
                View.VISIBLE else progressView.visibility = View.GONE
            is SwipeRefreshLayout ->
                progressView.isRefreshing = progress.isLoading
        }
    }

    private fun initStateScope() {
        stateJob = SupervisorJob()
        uiStateScope = CoroutineScope(stateJob + Dispatchers.Main.immediate)
    }

    @get:LayoutRes
    protected abstract val getLayoutResource: Int

    protected abstract fun initViews(view: View)

    protected abstract fun initDependencyInjection()

    protected abstract fun initViewModel()

    protected abstract fun initialize()

    protected abstract fun setListeners()
}