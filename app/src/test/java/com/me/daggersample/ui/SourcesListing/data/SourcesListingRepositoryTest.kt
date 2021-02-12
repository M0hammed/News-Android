package com.me.daggersample.ui.SourcesListing.data

import com.me.daggersample.model.base.ErrorTypes
import com.me.daggersample.model.base.Status
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.validator.INetworkValidator
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SourcesListingRepositoryTest {
    private val tag = "sourcesTag"

    @MockK
    private lateinit var networkValidator: INetworkValidator

    @MockK
    private lateinit var remoteDataSource: IRemoteDataSource

    private lateinit var sourceListingRepository: SourcesListingRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        sourceListingRepository = SourcesListingRepository(remoteDataSource, networkValidator)
    }

    @Test
    fun `given NetworkValidator, when call getNews(), then return Status_NoNetwork`() =
        runBlockingTest {
            // GIVEN
            every { networkValidator.isConnected() } returns (false)

            // WHEN
            val newsStatusFlow = sourceListingRepository.getNews()

            // THEN
            val actualValue = Status.Error(ErrorTypes.NoNetwork)
            val expectedValue = newsStatusFlow.single()
            assertEquals(actualValue, expectedValue)
        }

    @Test
    fun `given NetworkValidator, when call getNews(), then verify call iRemoteDataSource_getNews()`() =
        runBlockingTest {
            // GIVEN
            every { networkValidator.isConnected() } returns (true)
            every { remoteDataSource.getNews() } returns
                flowOf(Status.Error(ErrorTypes.ServerError(null)))

            // WHEN
            val newsStatusFlow = sourceListingRepository.getNews()

            // THEN
            val actualValue = newsStatusFlow.single()
            val expectedValue = Status.Error(ErrorTypes.ServerError(null))
            assertEquals(expectedValue, actualValue)

            verify { remoteDataSource.getNews() }
        }

    @Test
    fun `given RemoteDataSource, when call cancelApiCall, then verify called successfully`() {
        // GIVEN
        justRun { remoteDataSource.cancelApiCall(tag) }

        // WHEN
        sourceListingRepository.cancelApiCall()

        // THEN
        verify { remoteDataSource.cancelApiCall(tag) }
    }
}
