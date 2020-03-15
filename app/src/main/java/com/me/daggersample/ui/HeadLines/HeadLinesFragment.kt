package com.me.daggersample.ui.HeadLines

import android.os.Bundle
import android.view.View
import com.me.daggersample.R
import com.me.daggersample.base.BaseFragment
import com.me.daggersample.source.remote.apiInterface.ConstantsKeys

class HeadLinesFragment : BaseFragment<HeadLinesViewModel>() {
    companion object {
        const val TAG = "HeadLinesFragment"
        fun newInstance(sourceTag: String):HeadLinesFragment = HeadLinesFragment().apply {
            Bundle().apply {
                putString(ConstantsKeys.BundleKeys.SOURCES_KEY, sourceTag)
            }
        }
    }

    override val getLayoutResource: Int
        get() = R.layout.fragment_head_lines

    override fun initViews(view: View) {

    }

    override fun initDependencyInjection() {

    }

    override fun initViewModel() {

    }

    override fun initialize() {

    }

    override fun setListeners() {

    }
}