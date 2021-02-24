package com.me.daggersample.ui.SourcesListing.presentation

import com.me.daggersample.TestCoroutineRule
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.base.ErrorTypes
import com.me.daggersample.model.base.Status
import com.me.daggersample.model.source.Sources
import com.me.daggersample.ui.SourcesListing.data.ISourcesListingRepository
import com.me.daggersample.validator.INetworkValidator
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SourcesListingViewModelTest {

    @MockK
    lateinit var networkValidator: INetworkValidator

    @MockK
    lateinit var sourceListingRepository: ISourcesListingRepository

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    private lateinit var sourcesListingViewModel: SourcesListingViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sourcesListingViewModel =
            SourcesListingViewModel(sourceListingRepository, testCoroutineRule.dispatcher)
    }

    @Test
    fun `given noNetwork connection, then return NoNetwork Error Type`() = runBlockingTest {
        // GIVEN
        every { sourceListingRepository.getNews() } returns flowOf(Status.Error(ErrorTypes.NoNetwork))
        // WHEN
        sourcesListingViewModel.getNewsListing()

        // THEN - error type is noNetwork and should show error layout
        val errorType =
            sourcesListingViewModel.sourcesListingState.first()
        assertEquals(Status.Error(ErrorTypes.NoNetwork), errorType)

        val shouldShowErrorLayout = sourcesListingViewModel.errorState.first()
        assertTrue(shouldShowErrorLayout)
    }

    @Test
    fun `given noNetwork connection, then return NoNetwork Error Type and emit error message`() =
        runBlockingTest {
            // GIVEN
            sourcesListingViewModel.cashedSourcesList = generateSuccessNewsList()
            every { networkValidator.isConnected() } returns false

            // WHEN
            sourcesListingViewModel.getNewsListing()

            // THEN - error type is noNetwork and should show error layout
            val actualStatus =
                sourcesListingViewModel.sourcesListingState.first()
            assertEquals(Status.Idle, actualStatus)

            val shouldShowError = sourcesListingViewModel.errorState.first()
            assertFalse(shouldShowError)
        }

    @Test
    fun `given cached data, when call getNews and return error, then emit idle status`() =
        runBlockingTest {
            // GIVEN
            sourcesListingViewModel.cashedSourcesList = generateSuccessNewsList()
            val apiResponse = ApiResponse<ArrayList<Sources>>()
            apiResponse.result = arrayListOf()
            every { sourceListingRepository.getNews() } returns
                flowOf(Status.Success(apiResponse))
            // WHEN
            sourcesListingViewModel.getNewsListing()

            // THEN
            val first = sourcesListingViewModel.sourcesListingState.first()
            assertEquals(first, Status.Idle)
        }
}

private fun generateSuccessNewsList(): ArrayList<Sources> {
    val source = Sources(
        "id",
        "news title",
        "news description",
        "https://newsUrl.com",
        "news",
        "en",
        "EG"
    )

    val apiResponse = ApiResponse<ArrayList<Sources>>()
    apiResponse.status = "ok"
    apiResponse.result = arrayListOf(source, source, source)
    return arrayListOf(source, source, source)
}
