package com.me.daggersample.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.me.daggersample.model.networkData.ErrorModel
import com.me.daggersample.utils.SingleLiveEvent
import rx.subjects.PublishSubject

open class BaseViewModel : ViewModel() {
    protected val _errorMessage by lazy { SingleLiveEvent<ErrorModel>() }
    val errorMessage: LiveData<ErrorModel>
        get() = _errorMessage

    protected val _showErrorLayout by lazy { SingleLiveEvent<ErrorModel>() }
    val showErrorLayout: LiveData<ErrorModel>
        get() = _showErrorLayout

    protected val _hideErrorLayout by lazy { SingleLiveEvent<Nothing>() }
    val hideErrorLayout: LiveData<Nothing>
        get() = _hideErrorLayout
}