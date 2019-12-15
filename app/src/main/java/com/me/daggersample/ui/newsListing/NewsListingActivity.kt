package com.me.daggersample.ui.newsListing

import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import com.me.daggersample.R
import com.me.daggersample.base.BaseActivity
import com.me.daggersample.extentions.replaceFragment
import kotlinx.android.synthetic.main.appbar_layout.*

class NewsListingActivity : BaseActivity() {
    override fun getLayoutResource(): Int = R.layout.activity_main

    override fun initViews() {
        setToolbar(toolbar, getString(R.string.news_listing))
    }

    override fun initialize(savedInstanceState: Bundle?) {
        supportFragmentManager.replaceFragment(
            R.id.contentMainLayout,
            NewsListingFragment.newInstance(),
            NewsListingFragment.TAG
        )

    }

    override fun setListeners() {
    }

    override fun hasToolbar(): Boolean = true

    override fun isBackEnabled(): Boolean = false

    override fun screenToolbar(): Toolbar? = toolbar
}