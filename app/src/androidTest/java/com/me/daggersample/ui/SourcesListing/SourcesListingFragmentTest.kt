package com.me.daggersample.ui.SourcesListing

import android.os.Bundle
import android.view.View
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.me.daggersample.R
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Test


@MediumTest
class SourcesListingFragmentTest {


    lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register()
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister()
    }

    @Test
    fun showSourceListOnFirstLaunch() {
        // GIVEN - list of source items
        mockWebServer.dispatcher = dispatcher

        // WHEN - launch sources listing fragment
        launchFragmentInContainer<SourcesListingFragment>(Bundle(), R.style.AppTheme)

        // THEN - show the list items and hide loading progress

        onView(withId(R.id.layoutError)).check(matches(not(isDisplayed())))

        onView(withId(R.id.rvApp))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rvApp))
            .check(matches(atPosition(0, hasDescendant(withText("ABC News")))))
    }

    @Test
    fun showNoDataErrorLayoutWheEmptyListRetrieved() {
        // GIVEN - list of source items
        mockWebServer.dispatcher = emptyList

        // WHEN - launch sources listing fragment
        launchFragmentInContainer<SourcesListingFragment>(Bundle(), R.style.AppTheme)

        // THEN - show the list items and hide loading progress

        onView(withId(R.id.layoutError)).check(matches(isDisplayed()))

        onView(withId(R.id.tvErrorMessage)).check(matches(withText("No Data")))
    }

    private val dispatcher: Dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(200).setBody(
                "{\n" +
                        "   \"status\":\"ok\",\n" +
                        "   \"sources\":[\n" +
                        "      {\n" +
                        "         \"id\":\"abc-news\",\n" +
                        "         \"name\":\"ABC News\",\n" +
                        "         \"description\":\"Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com.\",\n" +
                        "         \"url\":\"https://abcnews.go.com\",\n" +
                        "         \"category\":\"general\",\n" +
                        "         \"language\":\"en\",\n" +
                        "         \"country\":\"us\"\n" +
                        "      }," +
                        "       {\n" +
                        "         \"id\":\"abc-news-au\",\n" +
                        "         \"name\":\"ABC News (AU)\",\n" +
                        "         \"description\":\"Australia's most trusted source of local, national and world news. Comprehensive, independent, in-depth analysis, the latest business, sport, weather and more.\",\n" +
                        "         \"url\":\"http://www.abc.net.au/news\",\n" +
                        "         \"category\":\"general\",\n" +
                        "         \"language\":\"en\",\n" +
                        "         \"country\":\"au\"\n" +
                        "      }" +
                        "]" +
                        "}"
            )
        }

    }

    private val emptyList: Dispatcher = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(200).setBody(
                "{\n" +
                        "   \"status\":\"ok\",\n" +
                        "   \"sources\":[\n" +
                        "]" +
                        "}"
            )
        }

    }

    private fun atPosition(position: Int, itemMatcher: Matcher<View?>): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has item at position $position: ")
                itemMatcher.describeTo(description)
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                val viewHolder = view.findViewHolderForAdapterPosition(position)
                    ?: // has no item on such position
                    return false
                return itemMatcher.matches(viewHolder.itemView)
            }
        }
    }


}