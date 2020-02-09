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
}