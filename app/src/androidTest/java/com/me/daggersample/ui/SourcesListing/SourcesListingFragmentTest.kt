package com.me.daggersample.ui.SourcesListing

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.me.daggersample.R
import com.me.daggersample.ui.SourcesListing.presentation.SourcesListingActivity
import com.me.daggersample.ui.SourcesListing.presentation.SourcesListingFragment
import com.me.daggersample.utils.checker.RecyclerViewChecker
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@MediumTest
class SourcesListingFragmentTest {


    lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register()
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
        //ActivityScenario.launch(SourcesListingActivity::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister()
    }

    @Test
    fun showSourceListOnFirstLaunch() {
        // GIVEN - list of source items
        mockWebServer.dispatcher = SourcesMockServer.getSuccessSourcesList()

        // WHEN - launch sources listing fragment
        launchFragmentInContainer<SourcesListingFragment>(Bundle(), R.style.AppTheme)

        // THEN - show the list items and hide loading progress
        Thread.sleep(1000)
        onView(withId(R.id.layoutError)).check(matches(not(isDisplayed())))

        onView(withId(R.id.rvApp))
            .check(matches(isDisplayed()))

        onView(withId(R.id.rvApp))
            .check(matches(RecyclerViewChecker.atPosition(0, hasDescendant(withText("ABC News")))))
    }

    @Test
    fun showNoDataErrorLayoutWheEmptyListRetrieved() {
        // GIVEN - list of source items
        mockWebServer.dispatcher = SourcesMockServer.getEmptySourceList()

        // WHEN - launch sources listing fragment
        launchFragmentInContainer<SourcesListingFragment>(Bundle(), R.style.AppTheme)

        // THEN - show the list items and hide loading progress
        Thread.sleep(1000)

        onView(withId(R.id.layoutError)).check(matches(isDisplayed()))

        onView(withId(R.id.tvErrorMessage)).check(matches(withText("No data found")))
    }

    @Test
    fun showErrorLayoutWithMessageWhenApiFail() {
        // GIVEN - Unauthorized error
        mockWebServer.dispatcher = SourcesMockServer.getUnauthorizedError()

        // WHEN - launch sources listing fragment
        launchFragmentInContainer<SourcesListingFragment>(Bundle(), R.style.AppTheme)

        // THEN - show the list items and hide loading progress
        Thread.sleep(1000)

        onView(withId(R.id.layoutError)).check(matches(isDisplayed()))

        onView(withId(R.id.tvErrorMessage)).check(matches(withText("Your API key is missing. Append this to the URL with the apiKey param, or use the x-api-key HTTP header.")))
    }
}