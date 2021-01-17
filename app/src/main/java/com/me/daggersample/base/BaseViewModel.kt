package com.me.daggersample.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.me.daggersample.model.base.Progress
import com.me.daggersample.model.base.Progress.*
import com.me.daggersample.model.networkData.ErrorModel
import com.me.daggersample.utils.SingleLiveEvent
import kotlinx.coroutines.flow.*

open class BaseViewModel : ViewModel() {
    private val _messageState by lazy { MutableSharedFlow<ErrorModel>() }
    val messageState: SharedFlow<ErrorModel>
        get() = _messageState

    protected val _errorLayoutVisibility by lazy { SingleLiveEvent<ErrorModel>() }
    val errorLayoutVisibility: LiveData<ErrorModel>
        get() = _errorLayoutVisibility

    private val _progressState by lazy { MutableSharedFlow<Progress>() }
    val progressState: SharedFlow<Progress>
        get() = _progressState

    protected suspend fun emitMessage(errorModel: ErrorModel) {
        _messageState.emit(errorModel)
    }

    protected suspend fun handleProgressVisibility(
        visibility: Boolean,
        isForceRefresh: Boolean = false,
        isLoadMore: Boolean = false
    ) {
        when {
            !isForceRefresh && !isLoadMore -> _progressState.emit(Main(visibility))
            isForceRefresh && !isLoadMore -> _progressState.emit(Refresh(visibility))
            else -> _progressState.emit(Paging(visibility))
        }
    }
}