package com.me.daggersample.ui.SourcesListing

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.me.daggersample.R
import com.me.daggersample.app.DaggerSampleApplication
import com.me.daggersample.base.BaseFragment
import com.me.daggersample.base.OnListItemClickListener
import com.me.daggersample.model.base.ErrorTypes
import com.me.daggersample.model.base.Progress
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.handler.Status
import kotlinx.android.synthetic.main.app_recycler_layout.*
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.main_progress_bar.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


class SourcesListingFragment : BaseFragment<SourcesListingViewModel>(),
    OnListItemClickListener<Sources> {

    @Inject
    lateinit var newsListingViewModelFactory: SourcesListingViewModelFactory

    private lateinit var newsListingAdapter: SourcesListingAdapter

    companion object {
        const val TAG: String = "NewsListingFragmentTag"
        fun newInstance(): SourcesListingFragment = SourcesListingFragment()
    }

    override val getLayoutResource: Int
        get() = R.layout.fragment_source_listing

    override fun initViews(view: View) {
        newsListingAdapter = SourcesListingAdapter(requireContext(), this)
        rvApp.layoutManager = LinearLayoutManager(requireContext())
        rvApp.adapter = newsListingAdapter

    }

    override fun initDependencyInjection() {
        (activity?.application as DaggerSampleApplication).appComponent
            .getNewsListingComponentFactory()
            .create()
            .inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this, newsListingViewModelFactory
        ).get(SourcesListingViewModel::class.java)
    }

    override fun initialize() {
        uiStateScope.launch {
            viewModel.sourcesListingState.collect(::onSourceListingStatusRetrieved)
        }

        uiStateScope.launch {
            viewModel.errorState.collect {
                layoutError.isVisible = it
            }
        }

        uiStateScope.launch {
            viewModel.progressState.collect {
                when (it) {
                    is Progress.Main -> handleProgressVisibility(it, mainProgress)
                    is Progress.Refresh -> handleProgressVisibility(it, swipeRefresh)
                }
            }
        }

        viewModel.getNewsListing()
    }

    private fun onSourceListingStatusRetrieved(sourceListingStatus: Status<ArrayList<Sources>>) {
        when (sourceListingStatus) {
            is Status.Success -> onSourceListingRetrievedSuccessfully(sourceListingStatus.data!!)
            is Status.Error -> onErrorRetrieved(sourceListingStatus.errorTypes)
            else -> {
            }
        }
    }

    private fun onSourceListingRetrievedSuccessfully(sourceListing: ArrayList<Sources>) {
        newsListingAdapter.insertAll(sourceListing)
    }

    private fun onErrorRetrieved(error: ErrorTypes) {
        error.errorIcon?.let { imgErrorIcon.setImageResource(it) }
        error.errorTitle?.let { tvErrorMessage.text = error.errorTitle }
        error.errorSubTitle?.let { tvErrorSubMessage.text = error.errorSubTitle }
    }

    override fun setListeners() {
        swipeRefresh.setOnRefreshListener { viewModel.refreshNewsListing() }
    }

    override fun onItemClicked(view: View?, model: Sources) {
        Log.e(TAG, "onItemClicked: ${model.description}")
    }
}