package com.me.daggersample.ui.HeadLines

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.me.daggersample.R
import com.me.daggersample.app.DaggerSampleApplication
import com.me.daggersample.base.BaseFragment
import com.me.daggersample.base.OnListItemClickListener
import com.me.daggersample.model.headLine.HeadLineModel
import com.me.daggersample.source.remote.apiInterface.ConstantsKeys
import kotlinx.android.synthetic.main.app_recycler_layout.*
import javax.inject.Inject

class HeadLinesFragment : BaseFragment<HeadLinesViewModel>(),
    OnListItemClickListener<HeadLineModel> {
    @Inject
    lateinit var headLinesViewModelFactory: HeadLinesViewModelFactory

    companion object {
        const val TAG = "HeadLinesFragment"
        fun newInstance(sourceTag: String): HeadLinesFragment = HeadLinesFragment().apply {
            arguments =
                Bundle().apply {
                    putString(ConstantsKeys.BundleKeys.SOURCES_KEY, sourceTag)
                }
        }
    }

    override val getLayoutResource: Int
        get() = R.layout.fragment_head_lines

    override fun initViews(view: View) {
        val headLinesAdapter = HeadLinesAdapter(requireContext(), this)
        rvApp.layoutManager = LinearLayoutManager(requireContext())
        rvApp.adapter = headLinesAdapter

    }

    override fun initDependencyInjection() {
        val sourceId = arguments?.getString(ConstantsKeys.BundleKeys.SOURCES_KEY, null)
        (activity?.application as DaggerSampleApplication).appComponent
            .getHeadLinesComponentFactory()
            .create(sourceId)
            .inject(this)
    }

    override fun initViewModel() {
        viewModel =
            ViewModelProvider(this, headLinesViewModelFactory).get(HeadLinesViewModel::class.java)
    }

    override fun initialize() {
        viewModel.getHeadLines().subscribe()
    }

    override fun setListeners() {

    }

    override fun onItemClicked(view: View?, model: HeadLineModel) {

    }
}