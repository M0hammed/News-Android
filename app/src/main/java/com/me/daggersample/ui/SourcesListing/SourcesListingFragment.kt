package com.me.daggersample.ui.SourcesListing

import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.me.daggersample.R
import com.me.daggersample.app.DaggerSampleApplication
import com.me.daggersample.base.BaseFragment
import com.me.daggersample.base.OnListItemClickListener
import com.me.daggersample.model.base.Progress
import com.me.daggersample.model.source.Sources
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

    @Inject
    lateinit var newsListingAdapter: SourcesListingAdapter

    companion object {
        const val TAG: String = "NewsListingFragmentTag"
        fun newInstance(): SourcesListingFragment = SourcesListingFragment()
    }

    override val getLayoutResource: Int
        get() = R.layout.fragment_source_listing

    override fun initViews(view: View) {
        rvApp.layoutManager = LinearLayoutManager(requireContext())
        rvApp.adapter = newsListingAdapter

    }

    override fun initDependencyInjection() {
        (activity?.application as DaggerSampleApplication).appComponent
            .getNewsListingComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this, newsListingViewModelFactory
        ).get(SourcesListingViewModel::class.java)
    }

    override fun initialize() {
        uiStateScope.launch {
            viewModel.sourcesListingState.collect {
                Log.e(TAG, "initialize: Status is $it")
            }
        }
        viewModel.errorLayoutVisibility.observe(viewLifecycleOwner, {
            bindErrorLayout(layoutError, it)
        })

        uiStateScope.launch {
            viewModel.progressState.collect {
                Log.e(TAG, "initialize: $it")
                when (it) {
                    is Progress.Main -> handleProgressVisibility(it, mainProgress)
                    is Progress.Refresh -> handleProgressVisibility(it, swipeRefresh)
                }
            }
        }

        viewModel.getNewsListing()
    }

    override fun setListeners() {
        swipeRefresh.setOnRefreshListener { viewModel.refreshNewsListing() }
    }

    override fun onItemClicked(view: View?, model: Sources) {
    }
}