package com.me.daggersample.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutResource())

        initViews()

        initialize(savedInstanceState)

        if (isBackEnabled()) {
            supportActionBar?.setDisplayShowHomeEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        setListeners()
    }

    protected fun setToolbar(toolbar: Toolbar?, title: String) {
        toolbar?.title = title
        setSupportActionBar(toolbar)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    protected abstract fun getLayoutResource(): Int

    protected abstract fun initViews()

    protected abstract fun initialize(savedInstanceState: Bundle?)

    protected abstract fun setListeners()

    abstract fun hasToolbar(): Boolean

    abstract fun isBackEnabled(): Boolean

    abstract fun screenToolbar(): Toolbar?

}