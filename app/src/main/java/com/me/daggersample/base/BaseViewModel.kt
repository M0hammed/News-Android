package com.me.daggersample.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.me.daggersample.model.base.Progress
import com.me.daggersample.model.base.Progress.*
import com.me.daggersample.model.networkData.ErrorModel
import com.me.daggersample.utils.SingleLiveEvent
import kotlinx.coroutines.flow.*

open class BaseViewModel : ViewModel() {
    protected val _messageState by lazy { MutableStateFlow<ErrorModel?>(null) }
    val messageState: Flow<ErrorModel>
        get() = _messageState.filterNotNull()

    protected val _errorLayoutVisibility by lazy { SingleLiveEvent<ErrorModel>() }
    val errorLayoutVisibility: LiveData<ErrorModel>
        get() = _errorLayoutVisibility

    private val _progressState by lazy { MutableStateFlow<Progress?>(null) }
    val progressState: Flow<Progress>
        get() = _progressState.filterNotNull()

    protected fun handleProgressVisibility(
        visibility: Boolean,
        isForceRefresh: Boolean = false,
        isLoadMore: Boolean = false
    ) {
        when {
            !isForceRefresh && !isLoadMore -> _progressState.tryEmit(Main(visibility))
            isForceRefresh && !isLoadMore -> _progressState.tryEmit(Refresh(visibility))
            else -> _progressState.tryEmit(Paging(visibility))
        }
    }
}