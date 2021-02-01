package com.me.daggersample.ui.headlines.presentation

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.me.daggersample.R
import com.me.daggersample.network.ErrorMockServer
import com.me.daggersample.network.HeadlinesMockServer
import com.me.daggersample.source.remote.apiInterface.ConstantsKeys
import com.me.daggersample.ui.HeadLines.presentation.HeadLinesFragment
import com.me.daggersample.utils.checker.RecyclerViewChecker.atPosition
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Test

@MediumTest
class HeadLinesFragmentTest {

    lateinit var mockWebServer: MockWebServer
    private val sourceValue = "sourceTag"

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register()
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
        //ActivityScenario.launch(HeadLinesActivity::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister()
    }

    @Test
    fun showHeadlinesListOnFirstLaunchWithGivenSourceId() {
        // GIVEN - headlines list items
        mockWebServer.dispatcher = HeadlinesMockServer.getSuccessHeadlinesList()

        // WHEN - launch headlines fragment
        val bundle = Bundle().apply {
            putString(ConstantsKeys.BundleKeys.SOURCES_KEY, sourceValue)
        }
        launchFragmentInContainer<HeadLinesFragment>(bundle, R.style.AppTheme)

        // THEN - verify show headlines list items and hide error layout
        Thread.sleep(1000)

        onView(withId(R.id.layoutError)).check(matches(not(isDisplayed())))

        onView(withId(R.id.rvApp))
            .check(
                matches(
                    atPosition(
                        0,
                        hasDescendant(withText("Covid: 'Lessons to be learnt' from NI vaccine row - Irish PM"))
                    )
                )
            )
    }

    @Test
    fun showErrorLayoutWhenPassEmptyBundle() {
        // GIVEN - headlines list items
        mockWebServer.dispatcher = HeadlinesMockServer.getSuccessHeadlinesList()

        // WHEN - launch headlines fragment
        launchFragmentInContainer<HeadLinesFragment>(Bundle(), R.style.AppTheme)

        // THEN - show error layout
        onView(withId(R.id.layoutError)).check(matches(isDisplayed()))
        onView(withId(R.id.tvErrorMessage)).check(matches(withText("Something wrong")))
    }

    @Test
    fun showNoDataErrorLayoutWheEmptyListRetrieved() {
        // GIVEN - list of headlines items
        mockWebServer.dispatcher = HeadlinesMockServer.getEmptyHeadlinesList()

        // WHEN - launch headlines listing fragment
        val bundle = Bundle().apply {
            putString(ConstantsKeys.BundleKeys.SOURCES_KEY, sourceValue)
        }
        launchFragmentInContainer<HeadLinesFragment>(bundle, R.style.AppTheme)

        // THEN - show the list items and hide loading progress
        Thread.sleep(1000)

        onView(withId(R.id.layoutError)).check(matches(isDisplayed()))

        onView(withId(R.id.tvErrorMessage)).check(matches(withText("No data found")))
    }

    @Test
    fun showErrorLayoutWithMessageWhenApiFail() {
        // GIVEN - Unauthorized error
        mockWebServer.dispatcher = ErrorMockServer.getUnauthorizedError()

        // WHEN - launch headlines listing fragment
        val bundle = Bundle().apply {
            putString(ConstantsKeys.BundleKeys.SOURCES_KEY, sourceValue)
        }
        launchFragmentInContainer<HeadLinesFragment>(bundle, R.style.AppTheme)

        // THEN - show the list items and hide loading progress
        Thread.sleep(1000)

        onView(withId(R.id.layoutError)).check(matches(isDisplayed()))

        onView(withId(R.id.tvErrorMessage)).check(matches(withText("Your API key is missing. Append this to the URL with the apiKey param, or use the x-api-key HTTP header.")))
    }

    @Test
    fun refreshHeadlinesListAfterApiFailed() {
        // GIVEN - list of headlines items
        mockWebServer.dispatcher = ErrorMockServer.getUnauthorizedError()

        // WHEN - launch headlines listing fragment
        val bundle = Bundle().apply {
            putString(ConstantsKeys.BundleKeys.SOURCES_KEY, sourceValue)
        }
        launchFragmentInContainer<HeadLinesFragment>(bundle, R.style.AppTheme)

        // THEN - show the list items and hide loading progress

        Thread.sleep(1000)

        onView(withId(R.id.layoutError)).check(matches(isDisplayed()))

        onView(withId(R.id.tvErrorMessage)).check(matches(withText("Your API key is missing. Append this to the URL with the apiKey param, or use the x-api-key HTTP header.")))

        mockWebServer.dispatcher = HeadlinesMockServer.getSuccessHeadlinesList()

        onView(withId(R.id.swipeRefresh)).perform(ViewActions.swipeDown())

        onView(withId(R.id.layoutError)).check(matches(not(isDisplayed())))

        onView(withId(R.id.rvApp))
            .check(matches(isDisplayed()))
            .check(
                matches(
                    atPosition(
                        0,
                        hasDescendant(withText("Covid: 'Lessons to be learnt' from NI vaccine row - Irish PM"))
                    )
                )
            )
    }
}