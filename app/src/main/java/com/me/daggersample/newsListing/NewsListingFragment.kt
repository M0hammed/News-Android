package com.me.daggersample.newsListing

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
        val disposable = viewModel.getNewsListing()
        addDisposable()?.add(disposable)
    }

    override fun setListeners() {

    }

    override fun getProgressMessage(): Int = R.string.loading
}