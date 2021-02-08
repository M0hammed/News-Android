package com.me.daggersample.ui.HeadLines.data

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
class HeadLinesRepositoryTest {
    private val tag = "headLinesTag"
    private val sourceId = "1"

    @MockK
    private lateinit var networkValidator: INetworkValidator

    @MockK
    private lateinit var remoteDataSource: IRemoteDataSource

    private lateinit var headlinesListingRepository: HeadLinesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        headlinesListingRepository = HeadLinesRepository(remoteDataSource, networkValidator)
    }

    @Test
    fun `given NetworkValidator, when call getHeadLinesList(), then return Status_NoNetwork`() =
        runBlockingTest {
            // GIVEN
            every { networkValidator.isConnected() } returns (false)

            // WHEN
            val headlinesStatusFlow = headlinesListingRepository.getHeadLinesList(sourceId)

            // THEN
            val actualValue = Status.Error(ErrorTypes.NoNetwork)
            val expectedValue = headlinesStatusFlow.single()
            assertEquals(actualValue, expectedValue)
        }

    @Test
    fun `given NetworkValidator, when call getHeadLinesList(), then verify call iRemoteDataSource_getHeadLinesList()`() =
        runBlockingTest {
            // GIVEN
            every { networkValidator.isConnected() } returns (true)
            every { remoteDataSource.getHeadLinesList(sourceId) } returns
                    flowOf(Status.Error(ErrorTypes.ServerError(null)))

            // WHEN
            val headlinesStatusFlow = headlinesListingRepository.getHeadLinesList(sourceId)

            // THEN
            val actualValue = headlinesStatusFlow.single()
            val expectedValue = Status.Error(ErrorTypes.ServerError(null))
            assertEquals(expectedValue, actualValue)

            verify { remoteDataSource.getHeadLinesList(sourceId) }
        }

    @Test
    fun `given RemoteDataSource, when call cancelApiCall, then verify called successfully`() {
        // GIVEN
        justRun { remoteDataSource.cancelApiCall(tag) }

        // WHEN
        headlinesListingRepository.cancelApiCall()

        // THEN
        verify { remoteDataSource.cancelApiCall(tag) }
    }
}