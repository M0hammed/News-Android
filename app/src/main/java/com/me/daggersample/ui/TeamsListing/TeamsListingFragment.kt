package com.me.daggersample.ui.TeamsListing

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.me.daggersample.R
import com.me.daggersample.app.DaggerSampleApplication
import com.me.daggersample.base.BaseFragment
import com.me.daggersample.base.OnListItemClickListener
import com.me.daggersample.extentions.makeSuccessMessage
import com.me.daggersample.model.team.Teams
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_recycler_layout.*
import javax.inject.Inject


class TeamsListingFragment : BaseFragment<TeamsListingViewModel>(),
    OnListItemClickListener<Teams> {

    @Inject
    lateinit var newsListingViewModelFactory: TeamsListingViewModelFactory
    private lateinit var newsListingAdapter: TeamsListingAdapter

    companion object {
        const val TAG: String = "NewsListingFragmentTag"
        fun newInstance(): TeamsListingFragment = TeamsListingFragment()
    }

    override val getLayoutResource: Int
        get() = R.layout.fragment_news_listing

    override fun initViews(view: View) {
        rvApp.layoutManager = LinearLayoutManager(requireContext())
        newsListingAdapter = TeamsListingAdapter(requireContext(), this)
        rvApp.adapter = newsListingAdapter

    }

    override fun initDependencyInjection() {
        (activity?.application as DaggerSampleApplication).appComponent
            .getNewsListingComponentBuilder()
            .build()
            .inject(this)
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(
            this, newsListingViewModelFactory
        ).get(TeamsListingViewModel::class.java)
    }

    override fun initialize() {
        disposable.add(
            viewModel.getNewsListing()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, { Log.e("xxx", "error message ${it.message}") })
        )
        viewModel.teamsListing.observe(this, Observer {
            Log.e("xxx", "name ${it[0].teamName}")
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

    override fun onItemClicked(view: View?, model: Teams) {
        model.teamName?.apply {
            Toast(requireContext())
                .makeSuccessMessage(requireContext(), this)
        }
    }

    override fun onDestroyView() {
        viewModel.cancelApiCall()
        super.onDestroyView()
    }
}