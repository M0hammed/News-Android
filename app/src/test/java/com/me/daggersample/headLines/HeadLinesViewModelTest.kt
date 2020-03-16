package com.me.daggersample.headLines

import android.view.View
import android.view.View.VISIBLE
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.google.gson.Gson
import com.me.daggersample.R
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.networkData.ErrorModel
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.source.remote.handler.ResponseStatus
import com.me.daggersample.ui.HeadLines.HeadLinesRepository
import com.me.daggersample.ui.HeadLines.HeadLinesViewModel
import com.me.daggersample.utils.Utils
import com.me.daggersample.validator.INetworkValidator
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

class HeadLinesViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule() // swap to background

    private val validator = Mockito.mock(INetworkValidator::class.java)
    private val remoteDataSource = Mockito.mock(IRemoteDataSource::class.java)
    private lateinit var headLinesRepository: HeadLinesRepository
    private lateinit var headLinesViewModelWithNullSourceID: HeadLinesViewModel
    private lateinit var headLinesViewModel: HeadLinesViewModel
    private lateinit var headLinesViewModelWithWrongSourceID: HeadLinesViewModel
    private val testingSourceId = ""
    private val wrongTestingSourceId = "wrongTestingSourceId"

    @Before
    fun setup() {
        headLinesRepository = HeadLinesRepository(remoteDataSource, validator)
        headLinesViewModelWithNullSourceID = HeadLinesViewModel(headLinesRepository, null)
        headLinesViewModel = HeadLinesViewModel(headLinesRepository, testingSourceId)
        headLinesViewModelWithWrongSourceID =
            HeadLinesViewModel(headLinesRepository, wrongTestingSourceId)
    }

    @Test
    fun `test source id null as Show Error Layout`() {
        headLinesViewModelWithNullSourceID.getHeadLines()
            .subscribe({}, {})
        val errorLayoutActualValue = headLinesViewModelWithNullSourceID.errorLayoutVisibility.value
        val errorLayoutExpectedValue = ErrorModel(
            message = R.string.something_went_wrong,
            subMessage = R.string.please_try_again,
            visibility = View.VISIBLE
        )
        Truth.assertThat(errorLayoutActualValue).isEqualTo(errorLayoutExpectedValue)
        Mockito.verify(remoteDataSource, Mockito.times(0))
            .getHeadLinesList("")
    }

    @Test
    fun `test no network as Show Error Layout`() {
        Mockito.`when`(validator.isConnected()).then { false }
        headLinesViewModel.getHeadLines()
            .subscribe({}, {})
        val errorLayoutActualValue = headLinesViewModel.errorLayoutVisibility.value
        val errorLayoutExpectedValue = ErrorModel(
            message = R.string.no_network,
            subMessage = R.string.please_try_again,
            visibility = View.VISIBLE
        )
        Truth.assertThat(errorLayoutActualValue).isEqualTo(errorLayoutExpectedValue)
        Mockito.verify(remoteDataSource, Mockito.times(0))
            .getHeadLinesList(testingSourceId)
    }

    @Test
    fun `test api response with wrong source id`() {
        val headLinesWithEmptyList =
            Utils.`generate json reader`("src/test/res/empty_headlines_json_file")
        val headLines =
            Gson().fromJson<ApiResponse<ArrayList<Sources>>>(
                headLinesWithEmptyList, ApiResponse::class.java
            )
        Mockito.`when`(remoteDataSource.getHeadLinesList(wrongTestingSourceId))
            .then { Observable.just(ResponseStatus.Success(data = headLines)) }

        headLinesViewModelWithWrongSourceID.getHeadLines()
            .subscribe({}, {})

        val errorLayoutActualValue = headLinesViewModelWithWrongSourceID.errorLayoutVisibility.value
        val errorLayoutExpectedValue = ErrorModel(
            message = R.string.no_data,
            subMessage = R.string.please_try_again,
            visibility = VISIBLE
        )
        Truth.assertThat(errorLayoutActualValue).isEqualTo(errorLayoutExpectedValue)
        val actualHeadListValue = headLinesViewModelWithWrongSourceID.healLinesList.value
        Truth.assertThat(actualHeadListValue).isEmpty()
        Mockito.verify(remoteDataSource, Mockito.times(1))
            .getHeadLinesList(wrongTestingSourceId)
    }
}