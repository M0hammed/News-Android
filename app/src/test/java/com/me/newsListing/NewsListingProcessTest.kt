package com.me.newsListing

import com.me.daggersample.common.INVALID_CUSTOMER_MOBILE
import com.me.daggersample.network.apiInterface.NewsListingApiInterface
import com.me.daggersample.ui.newsListing.NewsListingProcessor
import com.me.daggersample.ui.newsListing.NewsListingRepository
import com.me.daggersample.network.handler.ResponseErrorException
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class NewsListingProcessTest {

    val newsListingApiInterface = Mockito.mock(NewsListingApiInterface::class.java)
    val newsListingRepository = NewsListingRepository(newsListingApiInterface)

    @Test
    fun test_valid_mobile_num_fail() {
        val newsListingProcessor = NewsListingProcessor(newsListingRepository, "123456")
        try {
            newsListingProcessor.validate()
        } catch (ex: ResponseErrorException) {
            Assert.assertEquals(ex.errorModel.code, INVALID_CUSTOMER_MOBILE)
        }
    }

    @Test
    fun test_valid_mobile_num() {
        val newsListingProcessor = NewsListingProcessor(newsListingRepository, "01090611006")
        try {
            newsListingProcessor.validate()
        } catch (ex: ResponseErrorException) {
            Assert.assertEquals(ex.errorModel.code, INVALID_CUSTOMER_MOBILE)
        }
    }
}