package com.me.daggersample.ui.HeadLines.presentation

import androidx.lifecycle.viewModelScope
import com.me.daggersample.R
import com.me.daggersample.base.BaseViewModel
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.base.ErrorModel
import com.me.daggersample.model.base.ErrorTypes
import com.me.daggersample.model.base.Status
import com.me.daggersample.model.headLine.HeadLineModel
import com.me.daggersample.ui.HeadLines.data.IHeadLinesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

class HeadLinesViewModel(
    private val headLinesRepository: IHeadLinesRepository,
    private val sourceId: String?,
    private val backgroundDispatcher: CoroutineContext = Dispatchers.IO
) : BaseViewModel() {

    private val _headLinesListingState =
        MutableStateFlow<Status<ArrayList<HeadLineModel>>>(Status.Idle)
    val headLinesListingState: StateFlow<Status<ArrayList<HeadLineModel>>>
        get() = _headLinesListingState

    private var cachedHeadLinesList: ArrayList<HeadLineModel>? = null

    fun refreshHeadLinesListing() {
        getHeadLinesListing(true, loadMore = false)
    }

    fun getHeadLinesListing() {
        getHeadLinesListing(forceRefresh = false, loadMore = false)
    }

    private fun getHeadLinesListing(
        forceRefresh: Boolean = false,
        loadMore: Boolean = false
    ) {
        if (cachedHeadLinesList.isNullOrEmpty() || forceRefresh)
            if (sourceId != null) {
                headLinesRepository.getHeadLinesList(sourceId)
                    .flowOn(backgroundDispatcher)
                    .onStart { doOnStart(forceRefresh, loadMore) }
                    .onCompletion { doOnCompletion(forceRefresh, loadMore) }
                    .map { mapHeadLinesList(it, forceRefresh) }
                    .onEach { emitStatus(it) }
                    .catch { cause: Throwable -> doOnError(forceRefresh, loadMore, cause) }
                    .launchIn(viewModelScope)
            } else {
                emitErrorState(true)
                emitStatus(Status.Error(ErrorTypes.UnknownError()))
            }
    }

    // show progress and hide error layout
    private fun doOnStart(forceRefresh: Boolean, loadMore: Boolean) {
        emitProgress(true, forceRefresh, loadMore)
        emitErrorState(false)
    }

    // hide progress layout
    private fun doOnCompletion(forceRefresh: Boolean, loadMore: Boolean) {
        emitProgress(false, forceRefresh, loadMore)
    }

    // hide progress layout and validate if should show error layout
    private suspend fun doOnError(forceRefresh: Boolean, loadMore: Boolean, cause: Throwable) {
        emitProgress(false, forceRefresh, loadMore)
        validateCachedData(ErrorTypes.UnknownError(cause))
    }

    private suspend fun mapHeadLinesList(
        it: Status<ApiResponse<ArrayList<HeadLineModel>>>, forceRefresh: Boolean
    ): Status<ArrayList<HeadLineModel>> {
        return when (it) {
            is Status.Success -> validateHeadLinesList(it.data?.articles, forceRefresh)
            is Status.Error -> validateCachedData(it.errorTypes)
            else -> Status.Idle
        }
    }

    // validate list Size and nullability
    private suspend fun validateHeadLinesList(
        sourcesList: ArrayList<HeadLineModel>?, forceRefresh: Boolean
    ): Status<ArrayList<HeadLineModel>> {
        return if (!sourcesList.isNullOrEmpty()) {
            updateCachedHeadLinesList(sourcesList, forceRefresh)
            Status.Success(sourcesList)
        } else {
            if (cachedHeadLinesList.isNullOrEmpty()) {
                validateCachedData(ErrorTypes.NoData)
            } else {
                emitMessage(ErrorModel(message = R.string.something_went_wrong))
                Status.Idle
            }
        }
    }


    // check cached data if should show error layout or show toast
    private suspend fun validateCachedData(errorType: ErrorTypes): Status<ArrayList<HeadLineModel>> {
        return if (cachedHeadLinesList.isNullOrEmpty()) {
            emitErrorState(true)
            Status.Error(errorType)
        } else {
            errorType.errorTitle?.let { emitMessage(ErrorModel(it)) }
            Status.Idle
        }
    }

    // update cached data with given new values
    private fun updateCachedHeadLinesList(
        headLinesList: ArrayList<HeadLineModel>, forceRefresh: Boolean
    ) {
        when {
            cachedHeadLinesList == null -> this.cachedHeadLinesList = headLinesList
            forceRefresh -> {
                this.cachedHeadLinesList?.clear()
                this.cachedHeadLinesList?.addAll(headLinesList)
            }
            else -> {
                this.cachedHeadLinesList?.addAll(headLinesList)
            }
        }
    }

    private fun emitStatus(headLinesStatus: Status<ArrayList<HeadLineModel>>) {
        _headLinesListingState.value = headLinesStatus
    }
}