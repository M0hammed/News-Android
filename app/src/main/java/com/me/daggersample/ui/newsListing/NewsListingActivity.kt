package com.me.daggersample.ui.newsListing

import android.os.Bundle
import com.me.daggersample.R
import com.me.daggersample.base.BaseActivity

class NewsListingActivity : BaseActivity() {

    override fun initComponents(savedInstanceState: Bundle?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.newsContainerLayout, NewsListingFragment(), "newsListingFragment").commit()
    }
}
