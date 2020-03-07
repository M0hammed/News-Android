package com.me.daggersample.newsListing

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.me.daggersample.R
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.networkData.ErrorModel
import com.me.daggersample.model.networkData.ErrorResponse
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.source.remote.handler.ResponseStatus
import com.me.daggersample.ui.SourcesListing.SourcesListingRepository
import com.me.daggersample.ui.SourcesListing.SourcesListingViewModel
import com.me.daggersample.validator.INetworkValidator
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.io.File
import java.io.FileReader
import kotlin.NullPointerException


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

    @Test
    fun `test api failed when call sources list`() {
        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(remoteDataSource.getTeamsList()).then {
            Observable.just(ResponseStatus.ApiFailed(500, ErrorResponse()))
        }

        sourceListingViewModel.getNewsListing()
            .doOnSubscribe { `test do on subscribe first call to api`() }
            .doOnError { `test do on error first call to api`() }
            .subscribe({}, {})

        val value = sourceListingViewModel.errorLayoutVisibility.value
        val expected = ErrorModel(visibility = VISIBLE)
        Truth.assertThat(value).isEqualTo(expected)
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNull()
        Truth.assertThat(sourceListingViewModel.mainProgress.value).isEqualTo(GONE)
    }

    @Test
    fun `test genera error when call sources list`() {
        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(remoteDataSource.getTeamsList()).then {
            Observable.just(
                ResponseStatus.Error(
                    message = R.string.something_went_wrong, subMessage = R.string.please_try_again
                )
            )
        }

        sourceListingViewModel.getNewsListing()
            .doOnSubscribe { `test do on subscribe first call to api`() }
            .doOnError { `test do on error first call to api`() }
            .subscribe({}, {})

        val value = sourceListingViewModel.errorLayoutVisibility.value
        val expected = ErrorModel(
            message = R.string.something_went_wrong,
            subMessage = R.string.please_try_again,
            visibility = VISIBLE
        )
        Truth.assertThat(value).isEqualTo(expected)
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNull()
        Truth.assertThat(sourceListingViewModel.mainProgress.value).isEqualTo(GONE)
    }

    @Test
    fun `test correct data when call sources api`() {
        val jsonReader = `generate json reader`("src/test/res/sources_json_file")
        val sources =
            Gson().fromJson<ApiResponse<ArrayList<Sources>>>(jsonReader, ApiResponse::class.java)
        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(remoteDataSource.getTeamsList()).then {
            Observable.just(ResponseStatus.Success(data = sources))
        }
        sourceListingViewModel.getNewsListing()
            .doOnSubscribe { `test do on subscribe first call to api`() }
            .doOnError { `test do on error first call to api`() }
            .subscribe({}, {})

        val actual = sourceListingViewModel.sourcesListing.value
        val expected = sources.result

        Truth.assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test use cashed data if no change`() {
        val jsonReader = `generate json reader`("src/test/res/sources_json_file")
        val sources =
            Gson().fromJson<ApiResponse<ArrayList<Sources>>>(jsonReader, ApiResponse::class.java)
        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(remoteDataSource.getTeamsList()).then {
            Observable.just(ResponseStatus.Success(data = sources))
        }
        sourceListingViewModel.getNewsListing()
            .doOnSubscribe { `test do on subscribe first call to api`() }
            .doOnError { `test do on error first call to api`() }
            .subscribe({}, {})

        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNotNull()
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNotEmpty()

        sourceListingViewModel.getNewsListing().subscribe({}, {})
        Truth.assertThat(sourceListingViewModel.mainProgress.value).isEqualTo(GONE)
        Truth.assertThat(sourceListingViewModel.refreshProgress.value).isEqualTo(null)
        Mockito.verify(remoteDataSource, Mockito.times(1)).getTeamsList()
    }

    @Test
    fun `test data issue at first time api called`() {
        val dataIssueJsonReader =
            `generate json reader`("src/test/res/data_issue_sources_json_file")
        val sources =
            Gson().fromJson<ApiResponse<ArrayList<Sources>>>(
                dataIssueJsonReader,
                ApiResponse::class.java
            )
        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(remoteDataSource.getTeamsList()).then {
            Observable.just(ResponseStatus.Success(data = sources))
        }
        sourceListingViewModel.getNewsListing()
            .doOnSubscribe { `test do on subscribe first call to api`() }
            .doOnError { `test do on error first call to api`() }
            .subscribe({}, {})

        Truth.assertThat(sourceListingViewModel.mainProgress.value).isEqualTo(GONE)
        Truth.assertThat(sourceListingViewModel.errorLayoutVisibility.value)
            .isEqualTo(ErrorModel(message = R.string.something_went_wrong, visibility = VISIBLE))
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNull()
        Mockito.verify(remoteDataSource, Mockito.times(1)).getTeamsList()
    }

    @Test
    fun `test no internet when api return data first time and force refresh`() {
        val jsonReader = `generate json reader`("src/test/res/sources_json_file")
        val sources =
            Gson().fromJson<ApiResponse<ArrayList<Sources>>>(jsonReader, ApiResponse::class.java)
        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(remoteDataSource.getTeamsList()).then {
            Observable.just(ResponseStatus.Success(data = sources))
        }
        sourceListingViewModel.getNewsListing()
            .doOnSubscribe { `test do on subscribe first call to api`() }
            .doOnError { `test do on error first call to api`() }
            .subscribe({}, {})


        Mockito.`when`(validator.isConnected()).then { false }
        sourceListingViewModel.getNewsListing(true)
            .doOnSubscribe { `test do on subscribe force refresh with cashed data`() }
            .doOnError { `test do on error force refresh with cashed data`() }
            .subscribe({}, {})

        val progressActualValue = sourceListingViewModel.refreshProgress.value
        val progressExpectedValue = GONE

        val errorMessageActualValue = sourceListingViewModel.errorMessage.value
        val errorMessageExpectedValue =
            ErrorModel(message = R.string.no_network, visibility = VISIBLE)

        Truth.assertThat(progressActualValue).isEqualTo(progressExpectedValue)
        Truth.assertThat(errorMessageActualValue).isEqualTo(errorMessageExpectedValue)
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNotNull()
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNotEmpty()
        Mockito.verify(remoteDataSource, Mockito.times(1)).getTeamsList()
    }

    @Test
    fun `test server error when api return data first time and force refresh`() {
        val jsonReader = `generate json reader`("src/test/res/sources_json_file")
        val sources =
            Gson().fromJson<ApiResponse<ArrayList<Sources>>>(jsonReader, ApiResponse::class.java)
        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(remoteDataSource.getTeamsList()).then {
            Observable.just(ResponseStatus.Success(data = sources))
        }
        sourceListingViewModel.getNewsListing()
            .doOnSubscribe { `test do on subscribe first call to api`() }
            .doOnError { `test do on error first call to api`() }
            .subscribe({}, {})

        Mockito.`when`(remoteDataSource.getTeamsList()).then {
            Observable.just(ResponseStatus.ServerError(serverMessage = "Don't Talk to me there is an error"))
        }

        sourceListingViewModel.getNewsListing(true)
            .doOnSubscribe { `test do on subscribe force refresh with cashed data`() }
            .doOnError { `test do on error force refresh with cashed data`() }
            .subscribe({}, {})

        val actualProgressValue = sourceListingViewModel.refreshProgress.value
        val expectedProgressValue = GONE
        Truth.assertThat(actualProgressValue).isEqualTo(expectedProgressValue)

        val errorMessageActualValue = sourceListingViewModel.errorMessage.value
        val errorMessageExpectedValue =
            ErrorModel(serverMessage = "Don't Talk to me there is an error", visibility = VISIBLE)
        Truth.assertThat(errorMessageActualValue).isEqualTo(errorMessageExpectedValue)

        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNotEmpty()
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNotNull()
        Mockito.verify(remoteDataSource, Mockito.times(2)).getTeamsList()
    }

    @Test
    fun `test api failed when api return data first time and force refresh`() {
        val jsonReader = `generate json reader`("src/test/res/sources_json_file")
        val sources =
            Gson().fromJson<ApiResponse<ArrayList<Sources>>>(jsonReader, ApiResponse::class.java)
        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(remoteDataSource.getTeamsList()).then {
            Observable.just(ResponseStatus.Success(data = sources))
        }
        sourceListingViewModel.getNewsListing()
            .doOnSubscribe { `test do on subscribe first call to api`() }
            .doOnError { `test do on error first call to api`() }
            .subscribe({}, {})

        Mockito.`when`(remoteDataSource.getTeamsList())
            .then {
                Observable.just(
                    ResponseStatus.ApiFailed(500, ErrorResponse(message = "Shit Server Failed :("))
                )
            }

        sourceListingViewModel.getNewsListing(true)
            .doOnSubscribe { `test do on subscribe force refresh with cashed data`() }
            .doOnError { `test do on error force refresh with cashed data`() }
            .subscribe({}, {})
        val progressActualValue = sourceListingViewModel.refreshProgress.value
        val progressExpectedValue = GONE
        Truth.assertThat(progressActualValue).isEqualTo(progressExpectedValue)
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNotEmpty()
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNotNull()
        val errorMessageActualValue = sourceListingViewModel.errorMessage.value
        val errorMessageExpectedValue =
            ErrorModel(serverMessage = "Shit Server Failed :(", visibility = VISIBLE)
        Truth.assertThat(errorMessageActualValue).isEqualTo(errorMessageExpectedValue)
        Mockito.verify(remoteDataSource, Mockito.times(2)).getTeamsList()
    }

    @Test
    fun `test data issue at first time api called and force refresh`() {
        val jsonReader = `generate json reader`("src/test/res/sources_json_file")
        val sources =
            Gson().fromJson<ApiResponse<ArrayList<Sources>>>(jsonReader, ApiResponse::class.java)
        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(remoteDataSource.getTeamsList()).then {
            Observable.just(ResponseStatus.Success(data = sources))
        }
        sourceListingViewModel.getNewsListing()
            .doOnSubscribe { `test do on subscribe first call to api`() }
            .doOnError { `test do on error first call to api`() }
            .subscribe({}, {})

        val dataIssueJsonReader =
            `generate json reader`("src/test/res/data_issue_sources_json_file")
        val sourcesDataIssues =
            Gson().fromJson<ApiResponse<ArrayList<Sources>>>(
                dataIssueJsonReader, ApiResponse::class.java
            )
        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(remoteDataSource.getTeamsList()).then {
            Observable.just(ResponseStatus.Success(data = sourcesDataIssues))
        }
        sourceListingViewModel.getNewsListing(forceRefresh = true)
            .doOnSubscribe { `test do on subscribe force refresh with cashed data`() }
            .doOnError { `test do on error force refresh with cashed data`() }
            .subscribe({}, {})

        Truth.assertThat(sourceListingViewModel.refreshProgress.value).isEqualTo(GONE)
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNotNull()
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNotEmpty()
        Truth.assertThat(sourceListingViewModel.errorMessage.value)
            .isEqualTo(ErrorModel(message = R.string.something_went_wrong))
        Mockito.verify(remoteDataSource, Mockito.times(2)).getTeamsList()
    }

    @Test
    fun `test empty data when api return data first time and force refresh`() {
        val jsonReader = `generate json reader`("src/test/res/sources_json_file")
        val sources =
            Gson().fromJson<ApiResponse<ArrayList<Sources>>>(jsonReader, ApiResponse::class.java)
        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(remoteDataSource.getTeamsList()).then {
            Observable.just(ResponseStatus.Success(data = sources))
        }
        sourceListingViewModel.getNewsListing()
            .doOnSubscribe { `test do on subscribe first call to api`() }
            .doOnError { `test do on error first call to api`() }
            .subscribe({}, {})

        val emptySources = ArrayList<Sources>()
        val apiResponse = ApiResponse<ArrayList<Sources>>()
        apiResponse.result = emptySources

        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(remoteDataSource.getTeamsList())
            .then { Observable.just(ResponseStatus.Success(data = apiResponse)) }
        sourceListingViewModel.getNewsListing(true)
            .doOnSubscribe { `test do on subscribe force refresh with cashed data`() }
            .doOnError { `test do on error force refresh with cashed data`() }
            .subscribe({}, {})
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNotNull()
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isEmpty()
        val listingActualValue = sourceListingViewModel.sourcesListing.value
        Truth.assertThat(listingActualValue).isEqualTo(emptySources)
        val errorLayoutActualValue = sourceListingViewModel.errorLayoutVisibility.value
        val errorLayoutExpectedValue = ErrorModel(message = R.string.no_data, visibility = VISIBLE)
        Truth.assertThat(errorLayoutActualValue).isEqualTo(errorLayoutExpectedValue)
        Mockito.verify(remoteDataSource, Mockito.times(2)).getTeamsList()
    }

    @Test
    fun `test do on error`() {
        Mockito.`when`(validator.isConnected()).then { true }
        Mockito.`when`(remoteDataSource.getTeamsList())
            .then { Observable.error<ApiResponse<ArrayList<Sources>>>(NullPointerException()) }
        sourceListingViewModel.getNewsListing()
            .doOnSubscribe { `test do on subscribe first call to api`() }
            .doOnError { `test do on error first call to api`() }
            .subscribe({}, {})

        Truth.assertThat(sourceListingViewModel.mainProgress.value).isEqualTo(GONE)
        Truth.assertThat(sourceListingViewModel.errorLayoutVisibility.value)
            .isEqualTo(ErrorModel(visibility = VISIBLE))
    }

    private fun `test do on subscribe force refresh with cashed data`() {
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNotNull()
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNotEmpty()
        Truth.assertThat(sourceListingViewModel.refreshProgress.value).isEqualTo(VISIBLE)
        Truth.assertThat(sourceListingViewModel.errorLayoutVisibility.value)
            .isEqualTo(ErrorModel(visibility = GONE))
    }

    private fun `test do on error force refresh with cashed data`() {
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNotNull()
        Truth.assertThat(sourceListingViewModel.testingCashedSourcesList).isNotEmpty()
        Truth.assertThat(sourceListingViewModel.refreshProgress.value).isEqualTo(GONE)
        Truth.assertThat(sourceListingViewModel.errorLayoutVisibility.value)
            .isEqualTo(ErrorModel(visibility = GONE))
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

    private fun `generate json reader`(fileName: String): JsonReader {
        val file = File(fileName)
        return JsonReader(FileReader(file))
    }
}