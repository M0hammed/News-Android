package com.me.daggersample.ui.HeadLines.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.me.daggersample.R
import com.me.daggersample.app.DaggerSampleApplication
import com.me.daggersample.base.BaseFragment
import com.me.daggersample.base.OnListItemClickListener
import com.me.daggersample.extentions.makeErrorMessage
import com.me.daggersample.model.base.ErrorTypes
import com.me.daggersample.model.base.Progress
import com.me.daggersample.model.base.Status
import com.me.daggersample.model.headLine.HeadLineModel
import com.me.daggersample.source.remote.apiInterface.ConstantsKeys
import kotlinx.android.synthetic.main.app_recycler_layout.*
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.main_progress_bar.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class HeadLinesFragment : BaseFragment(), OnListItemClickListener<HeadLineModel> {
    @Inject
    lateinit var headLinesViewModelFactory: HeadLinesViewModelFactory
    private lateinit var viewModel: HeadLinesViewModel

    private lateinit var headLinesAdapter: HeadLinesAdapter

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
        headLinesAdapter = HeadLinesAdapter(requireContext(), this)
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
        uiStateScope.launch {
            viewModel.messageState.collect {
                Toast(requireContext()).makeErrorMessage(
                    requireContext(), it.serverMessage ?: getString(it.message)
                )
            }
        }

        uiStateScope.launch {
            viewModel.headLinesListingState.collect(::onHeadLinesListingStatusRetrieved)
        }

        uiStateScope.launch {
            viewModel.errorState.collect {
                layoutError.isVisible = it
            }
        }

        uiStateScope.launch {
            viewModel.progressState.collect {
                when (it) {
                    is Progress.Main -> handleProgressVisibility(it, mainProgress)
                    is Progress.Refresh -> handleProgressVisibility(it, swipeRefresh)
                }
            }
        }

        viewModel.getHeadLinesListing()
    }

    private fun onHeadLinesListingStatusRetrieved(headLinesListingStatus: Status<ArrayList<HeadLineModel>>) {
        when (headLinesListingStatus) {
            is Status.Success -> onSourceListingRetrievedSuccessfully(headLinesListingStatus.data!!)
            is Status.Error -> onErrorRetrieved(headLinesListingStatus.errorTypes)
            else -> {
            }
        }
    }

    private fun onSourceListingRetrievedSuccessfully(headLinesListing: ArrayList<HeadLineModel>) {
        headLinesAdapter.insertAll(headLinesListing)
    }

    private fun onErrorRetrieved(error: ErrorTypes) {
        error.errorIcon?.let { imgErrorIcon.setImageResource(it) }
        error.errorTitle?.let { tvErrorMessage.text = error.errorTitle }
        error.errorSubTitle?.let { tvErrorSubMessage.text = error.errorSubTitle }
    }

    override fun setListeners() {
        swipeRefresh.setOnRefreshListener { viewModel.refreshHeadLinesListing() }
    }

    override fun onItemClicked(view: View?, model: HeadLineModel) {
        viewModel.refreshHeadLinesListing()
    }
}