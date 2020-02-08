package com.me.daggersample.ui.SourcesListing

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.me.daggersample.R
import com.me.daggersample.app.DaggerSampleApplication
import com.me.daggersample.base.BaseFragment
import com.me.daggersample.base.OnListItemClickListener
import com.me.daggersample.extentions.makeSuccessMessage
import com.me.daggersample.model.source.Sources
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_recycler_layout.*
import javax.inject.Inject


class SourcesListingFragment : BaseFragment<SourcesListingViewModel>(),
    OnListItemClickListener<Sources> {

    @Inject
    lateinit var newsListingViewModelFactory: SourcesListingViewModelFactory
    @Inject lateinit var newsListingAdapter: SourcesListingAdapter

    companion object {
        const val TAG: String = "NewsListingFragmentTag"
        fun newInstance(): SourcesListingFragment = SourcesListingFragment()
    }

    override val getLayoutResource: Int
        get() = R.layout.fragment_source_listing

    override fun initViews(view: View) {
        rvApp.layoutManager = LinearLayoutManager(requireContext())
//        newsListingAdapter = SourcesListingAdapter(requireContext(), this)
        rvApp.adapter = newsListingAdapter

    }

    override fun initDependencyInjection() {
        (activity?.application as DaggerSampleApplication).appComponent
            .getNewsListingComponentBuilder()
            .create(requireContext(),this)
            .inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this, newsListingViewModelFactory
        ).get(SourcesListingViewModel::class.java)
    }

    override fun initialize() {
        disposable.add(
            viewModel.getNewsListing()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, { Log.e("xxx", "error message ${it.message}") })
        )
        viewModel.sourcesListing.observe(this, Observer {
            newsListingAdapter.insertAll(it)
        })
    }

    override fun setListeners() {
        swipeRefresh.setOnRefreshListener {
            viewModel.getNewsListing()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, { Log.e("xxx", "error message ${it.message}") })
        }
    }

    override fun onItemClicked(view: View?, model: Sources) {
        model.name?.apply {
            Toast(requireContext())
                .makeSuccessMessage(requireContext(), this)
        }
    }

    override fun onDestroyView() {
        viewModel.cancelApiCall()
        super.onDestroyView()
    }
}