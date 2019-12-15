package com.me.daggersample.ui.newsListing

import com.me.daggersample.base.BaseProcessor
import com.me.daggersample.common.INVALID_CUSTOMER_MOBILE
import com.me.daggersample.data.NewsResponse
import com.me.daggersample.network.handler.NetworkOutcome
import com.me.daggersample.validator.MobileNumberShouldBeValid
import io.reactivex.subjects.PublishSubject

class NewsListingProcessor(val newsListingRepository: NewsListingRepository, val mobNum: String) :
    BaseProcessor<NewsResponse>() {
    override fun validate() {
        MobileNumberShouldBeValid(mobNum, INVALID_CUSTOMER_MOBILE).orThrow()
    }

    override fun process(): PublishSubject<NetworkOutcome<NewsResponse>> {
        return newsListingRepository.getListingNews()
    }
}