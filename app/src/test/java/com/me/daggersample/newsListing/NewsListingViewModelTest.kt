package com.me.daggersample.newsListing

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.me.daggersample.R
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.networkData.ErrorModel
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.source.remote.handler.ResponseStatus
import com.me.daggersample.ui.SourcesListing.SourcesListingRepository
import com.me.daggersample.ui.SourcesListing.SourcesListingViewModel
import com.me.daggersample.utils.RxSchedulerRule
import com.me.daggersample.validator.INetworkValidator
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class NewsListingViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule() // swap to background

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
    fun `test no network when call sources list`() {
        Mockito.`when`(validator.isConnected()).then { false }
        sourceListingViewModel.getNewsListing()
            .doOnSubscribe { `test do on subscribe first call to api`() }
            .doOnError { `test do on error first call to api`() }
            .subscribeOn(RxSchedulerRule.SCHEDULER_INSTANCE)
            .observeOn(RxSchedulerRule.SCHEDULER_INSTANCE)
            .subscribe({}, {})
        val value = sourceListingViewModel.errorLayoutVisibility.value
        val expected = ErrorModel(
            message = R.string.no_network,
            subMessage = R.string.please_try_again,
            errorIcon = R.drawable.like,
            visibility = VISIBLE
        )
        Truth.assertThat(value).isEqualTo(expected)
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNull()
        Truth.assertThat(sourceListingViewModel.mainProgress.value).isEqualTo(GONE)
    }

    @Test
    fun `test server error when call sources list`() {
        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(remoteDataSource.getTeamsList())
            .then { Observable.just(ResponseStatus.ServerError("Hi iam in error mode now") as ResponseStatus<ApiResponse<Nothing>>) }

        sourceListingViewModel.getNewsListing()
            .doOnSubscribe { `test do on subscribe first call to api`() }
            .doOnError { `test do on error first call to api`() }
            .subscribe({}, {})

        val value = sourceListingViewModel.errorLayoutVisibility.value
        val expected = ErrorModel(serverMessage = "Hi iam in error mode now", visibility = VISIBLE)
        Truth.assertThat(value).isEqualTo(expected)
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNull()
        Truth.assertThat(sourceListingViewModel.mainProgress.value).isEqualTo(GONE)
    }

    private fun `test do on subscribe first call to api`() {
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNull()
        Truth.assertThat(sourceListingViewModel.mainProgress.value).isEqualTo(VISIBLE)
        Truth.assertThat(sourceListingViewModel.errorLayoutVisibility.value)
            .isEqualTo(ErrorModel(visibility = GONE))
    }

    private fun `test do on error first call to api`() {
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNull()
        Truth.assertThat(sourceListingViewModel.mainProgress.value).isEqualTo(GONE)
        Truth.assertThat(sourceListingViewModel.errorLayoutVisibility.value)
            .isEqualTo(ErrorModel(visibility = VISIBLE))
    }
}