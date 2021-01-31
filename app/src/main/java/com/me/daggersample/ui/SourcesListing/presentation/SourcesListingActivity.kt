package com.me.daggersample.ui.SourcesListing.presentation

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.me.daggersample.R
import com.me.daggersample.base.BaseActivity
import com.me.daggersample.extentions.replaceFragment
import com.me.daggersample.ui.SourcesListing.presentation.SourcesListingFragment
import kotlinx.android.synthetic.main.appbar_layout.*

class SourcesListingActivity : BaseActivity() {

    override val layoutResource: Int
        get() = R.layout.activity_source_listing

    override fun initViews() {
        setToolbar(toolbar, getString(R.string.news_listing))
    }

    override fun initialize(savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
            supportFragmentManager.replaceFragment(
                R.id.contentMainLayout,
                SourcesListingFragment.newInstance(),
                SourcesListingFragment.TAG
            )

    }

    override fun setListeners() {
    }

    override fun hasToolbar(): Boolean = true

    override fun isBackEnabled(): Boolean = false

    override fun screenToolbar(): Toolbar? = toolbar
}