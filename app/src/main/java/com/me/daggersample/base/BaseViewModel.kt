package com.me.daggersample.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.me.daggersample.model.networkData.ErrorModel

open class BaseViewModel : ViewModel() {
    protected val _errorMessage = MutableLiveData<ErrorModel>()
    val errorMessage: LiveData<ErrorModel>
        get() = _errorMessage

    protected val _showErrorLayout = MutableLiveData<ErrorModel>()
    val showErrorLayout: LiveData<ErrorModel>
        get() = _showErrorLayout

    protected val _hideErrorLayout = MutableLiveData<Nothing>()
    val hideErrorLayout: LiveData<Nothing>
        get() = _hideErrorLayout
}