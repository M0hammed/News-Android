package com.me.daggersample.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.me.daggersample.model.networkData.ErrorModel
import com.me.daggersample.utils.SingleLiveEvent

open class BaseViewModel : ViewModel() {
    protected val _errorMessage by lazy { SingleLiveEvent<ErrorModel>() }
    val errorMessage: LiveData<ErrorModel>
        get() = _errorMessage

    protected val _errorLayoutVisibility by lazy { SingleLiveEvent<ErrorModel>() }
    val errorLayoutVisibility: LiveData<ErrorModel>
        get() = _errorLayoutVisibility

    private val _mainProgress by lazy { SingleLiveEvent<Int>() }
    val mainProgress: LiveData<Int>
        get() = _mainProgress

    protected fun handleProgressVisibility(
        visibility: Int,
        isForceRefresh: Boolean,
        isLoadMore: Boolean
    ) {
        when {
            !isForceRefresh && !isLoadMore -> _mainProgress.postValue(visibility)

        }
    }
}