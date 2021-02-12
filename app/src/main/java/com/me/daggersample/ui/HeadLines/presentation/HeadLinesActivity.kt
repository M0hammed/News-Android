package com.me.daggersample.ui.HeadLines.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.me.daggersample.R
import com.me.daggersample.base.BaseActivity
import com.me.daggersample.source.remote.apiInterface.ConstantsKeys
import kotlinx.android.synthetic.main.appbar_layout.*

class HeadLinesActivity : BaseActivity() {
    companion object {
        fun startActivity(context: Context, sourceKey: String) {
            val intent = Intent(context, HeadLinesActivity::class.java)
            intent.putExtra(ConstantsKeys.BundleKeys.SOURCES_KEY, sourceKey)
            context.startActivity(intent)
        }
    }

    override val layoutResource: Int
        get() = R.layout.activity_head_lines

    override fun initViews() {
        setToolbar(toolbar, getString(R.string.head_lines))
    }

    override fun initialize(savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction().replace(
                R.id.headLinesContainer,
                HeadLinesFragment.newInstance(
                    intent.getStringExtra(ConstantsKeys.BundleKeys.SOURCES_KEY) ?: ""
                ),
                HeadLinesFragment.TAG
            ).commit()
    }

    override fun setListeners() {
    }

    override fun hasToolbar(): Boolean = true

    override fun isBackEnabled(): Boolean = true

    override fun screenToolbar(): Toolbar? = toolbar
}
