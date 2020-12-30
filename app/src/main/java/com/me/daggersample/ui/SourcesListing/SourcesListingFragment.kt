package com.me.daggersample.ui.SourcesListing

import android.view.View
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.me.daggersample.R
import com.me.daggersample.app.DaggerSampleApplication
import com.me.daggersample.base.BaseFragment
import com.me.daggersample.base.OnListItemClickListener
import com.me.daggersample.model.source.Sources
import kotlinx.android.synthetic.main.app_recycler_layout.*
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.main_progress_bar.*
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
        viewModel.getNewsListing()

        viewModel.sourcesListing.observe(viewLifecycleOwner, Observer {
            newsListingAdapter.insertAll(it)
        })

        viewModel.errorLayoutVisibility.observe(viewLifecycleOwner, Observer {
            bindErrorLayout(layoutError, it)
        })

        viewModel.mainProgress.observe(viewLifecycleOwner, Observer {
            pbMainProgress.visibility = it
        })

        viewModel.refreshProgress.observe(viewLifecycleOwner, Observer {
            swipeRefresh.isRefreshing = it == VISIBLE
        })
    }

    override fun setListeners() {
        swipeRefresh.setOnRefreshListener {
            lifecycleScope.launch { viewModel.getNewsListing(true) }
        }
    }

    override fun onItemClicked(view: View?, model: Sources) {
    }
}