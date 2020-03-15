package com.me.daggersample.ui.HeadLines

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.me.daggersample.R
import com.me.daggersample.app.DaggerSampleApplication
import com.me.daggersample.base.BaseFragment
import com.me.daggersample.source.remote.apiInterface.ConstantsKeys
import javax.inject.Inject

class HeadLinesFragment : BaseFragment<HeadLinesViewModel>() {

    @Inject
    lateinit var headLinesViewModelFactory: HeadLinesViewModelFactory

    companion object {
        const val TAG = "HeadLinesFragment"
        fun newInstance(sourceTag: String): HeadLinesFragment = HeadLinesFragment().apply {
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
        (activity?.application as DaggerSampleApplication).appComponent
            .getHeadLinesComponentFactory()
            .create(arguments?.getString(ConstantsKeys.BundleKeys.SOURCES_KEY, null))
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, headLinesViewModelFactory).get(HeadLinesViewModel::class.java)
    }

    override fun initialize() {

    }

    override fun setListeners() {

    }
}