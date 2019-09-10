package com.me.daggersample.newsListing

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
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
