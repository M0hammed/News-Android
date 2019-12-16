package com.me.newsListing

import com.me.daggersample.common.NO_DATA
import com.me.daggersample.model.news.NewsModel
import com.me.daggersample.source.remote.apiInterface.RetrofitApisInterface
import com.me.daggersample.ui.newsListing.NewsListingRepository
import com.me.daggersample.ui.newsListing.NewsListingViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


class NewsListingViewModelTest {

    val newsListingApiInterface = Mockito.mock(RetrofitApisInterface::class.java)

    val newsListingRepository = NewsListingRepository(newsListingApiInterface)

    val newsListingVieModel = NewsListingViewModel(newsListingRepository)

    val newsList = ArrayList<NewsModel>()

    lateinit var newsItem: NewsModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        newsItem = NewsModel()
    }


    @Test
    fun test_news_list_has_no_data() {
        newsListingVieModel.validateNewsList(newsList)
        val value = newsListingVieModel.handleErrorMessage.value
        assertEquals(value?.code, NO_DATA)
    }

    @Test
    fun test_news_list_has_data() {
        newsList.add(newsItem)
        newsListingVieModel.validateNewsList(newsList)
        val value = newsListingVieModel.newsListing.value
        assertEquals(value!![0], newsItem)
    }
}

