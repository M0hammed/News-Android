package com.me.daggersample.ui

import com.me.daggersample.ui.SourcesListing.presentation.SourcesListingFragmentTest
import com.me.daggersample.ui.headlines.presentation.HeadLinesFragmentTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    SourcesListingFragmentTest::class,
    HeadLinesFragmentTest::class
)
class TestSuite