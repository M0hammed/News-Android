package com.me.daggersample.ui.newsListing

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.me.daggersample.base.BaseFragment
import com.me.daggersample.base.OnListItemClickListener
import com.me.daggersample.base.PaginationScrollListener
import com.me.daggersample.model.news.NewsModel
import com.me.daggersample.extentions.makeSuccessMessage
import com.me.daggersample.source.remote.handler.ResponseStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.app_recycler_layout.*
import javax.inject.Inject


class NewsListingFragment : BaseFragment<NewsListingViewModel>(),
    OnListItemClickListener<NewsModel> {
    private var currentPage = 1
    private var isLastPage = false
    private var totalPage = 10
    private var isLoading = false

    @Inject
    lateinit var newsListingViewModelFactory: NewsListingViewModelFactory
    private lateinit var newsListingAdapter: NewsListingAdapter

    companion object {
        const val TAG: String = "NewsListingFragmentTag"
        fun newInstance(): NewsListingFragment = NewsListingFragment()
    }

    override fun getLayoutResource(): Int = com.me.daggersample.R.layout.fragment_news_listing

    override fun initViews(view: View) {
        rvApp.layoutManager = LinearLayoutManager(requireContext())
        newsListingAdapter = NewsListingAdapter(requireContext(), this)
        rvApp.adapter = newsListingAdapter

    }

    override fun initViewModel() {
        viewModel =
            ViewModelProviders.of(this, newsListingViewModelFactory)
                .get(NewsListingViewModel::class.java)
    }

    override fun initialize() {
        addDisposable()?.add(
            viewModel.getNewsListing()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it is ResponseStatus.Success) {
                        val data = it.data?.result?.get(0)
                        Log.e("xxx", "title is ${data?.teamName}")
                        Log.e("xxx", "title is ${it}")
                    }
                }, { Log.e("xxx", "error message ${it.message}") })
        )

        addDisposable()?.add(viewModel.newsListing.subscribe {
            newsListingAdapter.insertAll(it)
        })
    }

    override fun setListeners() {
        swipeRefresh.setOnRefreshListener {
            viewModel.getNewsListing()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //                    if (it is ResponseStatus.Success) {
//                        val data = it.data?.result?.newsData?.get(0)
//                        Log.e("xxx", "title is ${data?.imageUrl}")
                    Log.e("xxx", "title is ${it}")
//                    }
                }, { Log.e("xxx", "error message ${it.message}") })
        }
    }

    override fun getProgressMessage(): Int = com.me.daggersample.R.string.loading

    override fun onItemClicked(view: View?, model: NewsModel) {
        requireContext().apply { Toast(this).makeSuccessMessage(this, model.newsTitle) }
    }

    private fun setRecyclerViewPagination(layoutManager: LinearLayoutManager) {
        rvApp.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage++
                isLastPage = false
            }

            override fun getTotalPageCount(): Int {
                return 10
            }

            override fun isLastPage(): Boolean = isLastPage

            override fun isLoading(): Boolean = isLoading
        })
    }
}