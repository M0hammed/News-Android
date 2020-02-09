package com.me.daggersample.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.me.daggersample.extentions.makeErrorMessage
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<V : BaseViewModel> : Fragment() {

    protected lateinit var viewModel: V
    protected var disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(getLayoutResource, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDependencyInjection()
        initViews(view)
        setListeners()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
        initialize()
        handleObservers()
    }

    private fun handleObservers() {
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Toast(requireContext()).makeErrorMessage(
                requireContext(), it?.serverMessage ?: getString(it.message)
            )
        })
    }

    override fun onDestroy() { // for disposing when destroy
        disposable.dispose()
        super.onDestroy()
    }

    @get:LayoutRes
    protected abstract val getLayoutResource: Int

    protected abstract fun initViews(view: View)

    protected abstract fun initDependencyInjection()

    protected abstract fun initViewModel()

    protected abstract fun initialize()

    protected abstract fun setListeners()
}