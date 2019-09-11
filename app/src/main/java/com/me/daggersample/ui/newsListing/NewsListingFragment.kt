package com.me.daggersample.ui.newsListing

import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.me.daggersample.R
import com.me.daggersample.base.BaseFragment
import javax.inject.Inject

class NewsListingFragment : BaseFragment<NewsListingViewModel>() {
    @Inject
    lateinit var newsListingViewModelFactory: NewsListingViewModelFactory

    override fun getLayoutResource(): Int =
        R.layout.fragment_news_listing

    override fun initViews(view: View) {
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProviders.of(this, newsListingViewModelFactory).get(NewsListingViewModel::class.java)
    }

    override fun initialize() {
        val viewModelDisposable = viewModel.getNewsListing()
        addDisposable()?.add(viewModelDisposable)

        addDisposable()?.add(viewModel.newsListing.subscribe {

        })
    }

    override fun setListeners() {

    }

    override fun getProgressMessage(): Int = R.string.loading
}