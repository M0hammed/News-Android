package com.me.daggersample.ui.SourcesListing

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.filters.MediumTest
import com.me.daggersample.R
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Test


@MediumTest
class SourcesListingFragmentTest {


    lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun warmUp() {
        mockWebServer.dispatcher = dispatcher
        launchFragmentInContainer<SourcesListingFragment>(Bundle(), R.style.AppTheme)
    }


    private val dispatcher: Dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(200).setBody(
                "" +
                        "\n" +
                        "\"{ " +
                        "   status\":\"ok\",\n" +
                        "   \"sources\":[\n" +
                        "      {\n" +
                        "         \"id\":\"abc-news\",\n" +
                        "         \"name\":\"ABC News\",\n" +
                        "         \"description\":\"Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com.\",\n" +
                        "         \"url\":\"https://abcnews.go.com\",\n" +
                        "         \"category\":\"general\",\n" +
                        "         \"language\":\"en\",\n" +
                        "         \"country\":\"us\"\n" +
                        "      }]}"
            )
        }

    }
}