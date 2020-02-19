package com.me.daggersample.newsListing

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.me.daggersample.R
import com.me.daggersample.model.networkData.ErrorModel
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.source.remote.handler.ResponseStatus
import com.me.daggersample.ui.SourcesListing.SourcesListingRepository
import com.me.daggersample.ui.SourcesListing.SourcesListingViewModel
import com.me.daggersample.utils.RxSchedulerRule
import com.me.daggersample.validator.INetworkValidator
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.util.*

class NewsListingViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule() // swap to background

//    @get:Rule
//    val rxSchedulerRule = RxSchedulerRule()

    private val validator = Mockito.mock(INetworkValidator::class.java)
    private val remoteDataSource = Mockito.mock(IRemoteDataSource::class.java)
    private lateinit var sourceListingRepository: SourcesListingRepository
    private lateinit var sourceListingViewModel: SourcesListingViewModel

    @Before
    fun setup() {
        sourceListingRepository = SourcesListingRepository(remoteDataSource, validator)
        sourceListingViewModel = SourcesListingViewModel(sourceListingRepository)
    }

    @Test
    fun `test no network when call news list`() {
        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(sourceListingRepository.getListingTeams())
            .then { Observable.just(ResponseStatus.NoNetwork()) }

        sourceListingViewModel.getNewsListing()
            .subscribeOn(RxSchedulerRule.SCHEDULER_INSTANCE)
            .observeOn(RxSchedulerRule.SCHEDULER_INSTANCE)
            .subscribe()
        val value = sourceListingViewModel.errorLayoutVisibility.value
        val expected = ErrorModel(
            message = R.string.no_network,
            subMessage = R.string.please_try_again,
            errorIcon = R.drawable.like,
            visibility = View.VISIBLE
        )
        Truth.assertThat(value).isEqualTo(expected)
    }
}