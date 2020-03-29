package com.me.daggersample.headLines

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth
import com.google.gson.Gson
import com.me.daggersample.R
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.headLine.HeadLineModel
import com.me.daggersample.model.networkData.ErrorModel
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.source.remote.handler.ResponseStatus
import com.me.daggersample.ui.HeadLines.HeadLinesRepository
import com.me.daggersample.ui.HeadLines.HeadLinesViewModel
import com.me.daggersample.utils.RxTestRules
import com.me.daggersample.utils.Utils
import com.me.daggersample.validator.INetworkValidator
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class HeadLinesViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule() // swap to background
    @get:Rule
    val rxJavaRules = RxTestRules()

    @Mock
    lateinit var validator: INetworkValidator
    @Mock
    lateinit var remoteDataSource: IRemoteDataSource
    @Mock
    lateinit var mainProgressObserver: Observer<Int>
    @Mock
    lateinit var errorLayoutObserver: Observer<ErrorModel>
    @Mock
    lateinit var headLinesListObserver: Observer<ArrayList<HeadLineModel>>

    private lateinit var headLinesRepository: HeadLinesRepository
    private lateinit var headLinesViewModelWithNullSourceID: HeadLinesViewModel
    private lateinit var headLinesViewModel: HeadLinesViewModel
    private lateinit var headLinesViewModelWithWrongSourceID: HeadLinesViewModel
    private val testingSourceId = "bbc-news"
    private val wrongTestingSourceId = "wrongTestingSourceId"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        headLinesRepository = HeadLinesRepository(remoteDataSource, validator)
        headLinesViewModelWithNullSourceID = HeadLinesViewModel(headLinesRepository, null)
        headLinesViewModel = HeadLinesViewModel(headLinesRepository, testingSourceId)
        headLinesViewModelWithWrongSourceID =
            HeadLinesViewModel(headLinesRepository, wrongTestingSourceId)
    }

    @Test
    fun `test source id null as Show Error Layout`() {
        Mockito.`when`(validator.isConnected()).then { true }
        headLinesViewModelWithNullSourceID.getHeadLines()
            .subscribe({}, {})
        val errorLayoutActualValue = headLinesViewModelWithNullSourceID.errorLayoutVisibility.value
        val errorLayoutExpectedValue = ErrorModel(
            message = R.string.something_went_wrong,
            subMessage = R.string.please_try_again,
            visibility = VISIBLE
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
            visibility = VISIBLE
        )
        Truth.assertThat(errorLayoutActualValue).isEqualTo(errorLayoutExpectedValue)
        Mockito.verify(remoteDataSource, Mockito.times(0))
            .getHeadLinesList(testingSourceId)
    }

    @Test
    fun `call getHeadLines() and then return exception and handle it`() {
        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(remoteDataSource.getHeadLinesList(testingSourceId))
            .then {
                Observable.error<ResponseStatus<ApiResponse<ArrayList<HeadLineModel>>>>(
                    NullPointerException()
                )
            }
        headLinesViewModel.mainProgress.observeForever(mainProgressObserver)
        headLinesViewModel.errorLayoutVisibility.observeForever(errorLayoutObserver)
        headLinesViewModel.getHeadLines().subscribe()
        Mockito.verify(mainProgressObserver, Mockito.times(2)).onChanged(any())
        Mockito.verify(errorLayoutObserver, Mockito.times(2)).onChanged(any())
        Truth.assertThat(headLinesViewModel.mainProgress.value).isNotNull()
        Truth.assertThat(headLinesViewModel.errorLayoutVisibility.value).isNotNull()
        Truth.assertThat(headLinesViewModel.mainProgress.value).isEqualTo(GONE)
        Truth.assertThat(headLinesViewModel.errorLayoutVisibility.value)
            .isEqualTo(ErrorModel(visibility = VISIBLE))
    }

    @Test
    fun `call getHeadLines() and then return ServerError Status`() {
        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(remoteDataSource.getHeadLinesList(testingSourceId)).then {
            Observable.just(
                ResponseStatus.ServerError(
                    serverMessage = "Hi something went wrong with server now",
                    subMessage = R.string.please_try_again
                )
            )
        }
        headLinesViewModel.mainProgress.observeForever(mainProgressObserver)
        headLinesViewModel.errorLayoutVisibility.observeForever(errorLayoutObserver)
        headLinesViewModel.getHeadLines().subscribe()
        Mockito.verify(mainProgressObserver, Mockito.times(3)).onChanged(any())
        Mockito.verify(errorLayoutObserver, Mockito.times(3)).onChanged(any())
        Truth.assertThat(headLinesViewModel.mainProgress.value).isNotNull()
        Truth.assertThat(headLinesViewModel.errorLayoutVisibility.value).isNotNull()
        Truth.assertThat(headLinesViewModel.mainProgress.value).isEqualTo(GONE)
        Truth.assertThat(headLinesViewModel.errorLayoutVisibility.value)
            .isEqualTo(
                ErrorModel(
                    visibility = VISIBLE,
                    serverMessage = "Hi something went wrong with server now",
                    subMessage = R.string.please_try_again
                )
            )
    }

    @Test
    fun `call getHeadLines() and the return empty data`() {
        Mockito.`when`(validator.isConnected()).then { true }
        val jsonReader = Utils.`generate json reader`("src/test/res/empty_headlines_json_file")
        val headLines =
            Gson().fromJson<ApiResponse<ArrayList<HeadLineModel>>>(
                jsonReader, ApiResponse::class.java
            )
        Mockito.`when`(remoteDataSource.getHeadLinesList(testingSourceId))
            .then { Observable.just(ResponseStatus.Success(headLines)) }

        headLinesViewModel.mainProgress.observeForever(mainProgressObserver)
        headLinesViewModel.errorLayoutVisibility.observeForever(errorLayoutObserver)
        headLinesViewModel.headLinesList.observeForever(headLinesListObserver)
        headLinesViewModel.getHeadLines().subscribe()
        Mockito.verify(mainProgressObserver, Mockito.times(2)).onChanged(any())
        Mockito.verify(errorLayoutObserver, Mockito.times(3)).onChanged(any())
        Mockito.verify(headLinesListObserver, Mockito.times(0)).onChanged(any())
        Truth.assertThat(headLinesViewModel.errorLayoutVisibility.value)
            .isEqualTo(
                ErrorModel(
                    message = R.string.no_data,
                    subMessage = R.string.please_try_again,
                    visibility = VISIBLE
                )
            )
        Truth.assertThat(headLinesViewModel.headLinesList.value).isNull()
    }

    @Test
    fun `call getHeadLines() and then return valid data`() {
        Mockito.`when`(validator.isConnected()).then { true }
        val jsonReader = Utils.`generate json reader`("src/test/res/head_lines_valid_data")
        val headLines =
            Gson().fromJson<ApiResponse<ArrayList<HeadLineModel>>>(
                jsonReader, ApiResponse::class.java
            )
        Mockito.`when`(remoteDataSource.getHeadLinesList(testingSourceId))
            .then { Observable.just(ResponseStatus.Success(headLines)) }

        headLinesViewModel.mainProgress.observeForever(mainProgressObserver)
        headLinesViewModel.errorLayoutVisibility.observeForever(errorLayoutObserver)
        headLinesViewModel.headLinesList.observeForever(headLinesListObserver)
        headLinesViewModel.getHeadLines().subscribe()

        Mockito.verify(mainProgressObserver, Mockito.times(2)).onChanged(any())
        Mockito.verify(errorLayoutObserver, Mockito.times(2)).onChanged(any())
        Mockito.verify(headLinesListObserver, Mockito.times(1)).onChanged(any())

        Truth.assertThat(headLinesViewModel.errorLayoutVisibility.value)
            .isEqualTo(ErrorModel(visibility = GONE))
        Truth.assertThat(headLinesViewModel.headLinesList.value).isEqualTo(headLines.articles)
    }
}