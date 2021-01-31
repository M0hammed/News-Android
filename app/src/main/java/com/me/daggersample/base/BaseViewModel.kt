package com.me.daggersample.base

import androidx.lifecycle.ViewModel
import com.me.daggersample.model.base.Progress
import com.me.daggersample.model.base.Progress.*
import com.me.daggersample.model.base.ErrorModel
import kotlinx.coroutines.flow.*

open class BaseViewModel : ViewModel() {
    private val _messageState by lazy { MutableSharedFlow<ErrorModel>() }
    val messageState: SharedFlow<ErrorModel>
        get() = _messageState

    private val _progressState by lazy { MutableStateFlow<Progress>(Main(false)) }
    val progressState: StateFlow<Progress>
        get() = _progressState

    private val _errorState by lazy { MutableStateFlow(false) }
    val errorState: StateFlow<Boolean>
        get() = _errorState

    protected suspend fun emitMessage(errorModel: ErrorModel) {
        _messageState.emit(errorModel)
    }

    protected fun emitProgress(
        visibility: Boolean,
        isForceRefresh: Boolean = false,
        isLoadMore: Boolean = false
    ) {
        when {
            !isForceRefresh && !isLoadMore -> _progressState.value = Main(visibility)
            isForceRefresh && !isLoadMore -> _progressState.value = Refresh(visibility)
            else -> _progressState.value = Paging(visibility)
        }
    }

    protected fun emitErrorState(shouldShowError: Boolean) {
        _errorState.value = shouldShowError
    }
}