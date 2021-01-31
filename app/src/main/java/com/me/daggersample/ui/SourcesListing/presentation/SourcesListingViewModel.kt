package com.me.daggersample.ui.SourcesListing.presentation

import androidx.lifecycle.viewModelScope
import com.me.daggersample.R
import com.me.daggersample.base.BaseViewModel
import com.me.daggersample.model.base.*
import com.me.daggersample.model.source.Sources
import com.me.daggersample.ui.SourcesListing.data.ISourcesListingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlin.coroutines.CoroutineContext

class SourcesListingViewModel(
    private val sourcesListingRepository: ISourcesListingRepository,
    private val backgroundDispatcher: CoroutineContext = Dispatchers.IO
) : BaseViewModel() {

    private val _sourcesListingState = MutableStateFlow<Status<ArrayList<Sources>>>(Status.Idle)
    val sourcesListingState: StateFlow<Status<ArrayList<Sources>>>
        get() = _sourcesListingState


    private var cashedSourcesList: ArrayList<Sources>? = null

    fun refreshNewsListing() {
        getNewsListing(true, loadMore = false)
    }

    fun getNewsListing() {
        getNewsListing(forceRefresh = false, loadMore = false)
    }

    private fun getNewsListing(
        forceRefresh: Boolean = false,
        loadMore: Boolean = false
    ) {
        if (cashedSourcesList.isNullOrEmpty() || forceRefresh)
            sourcesListingRepository.getNews()
                .flowOn(backgroundDispatcher)
                .onStart { doOnStart(forceRefresh, loadMore) }
                .onCompletion { doOnCompletion(forceRefresh, loadMore) }
                .map { mapNewsListing(it, forceRefresh) }
                .onEach { emitStatus(it) }
                .catch { cause: Throwable ->
                    doOnError(forceRefresh, loadMore, cause)
                }
                .launchIn(viewModelScope)
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

    private suspend fun mapNewsListing(
        it: Status<ApiResponse<ArrayList<Sources>>>, forceRefresh: Boolean
    ): Status<ArrayList<Sources>> {
        return when (it) {
            is Status.Success -> validateSourcesList(it.data?.result, forceRefresh)
            is Status.Error -> validateCachedData(it.errorTypes)
            else -> Status.Idle
        }
    }

    // validate list Size and nullability
    private suspend fun validateSourcesList(
        sourcesList: ArrayList<Sources>?, forceRefresh: Boolean
    ): Status<ArrayList<Sources>> {
        return if (!sourcesList.isNullOrEmpty()) {
            updateCachedNewsList(sourcesList, forceRefresh)
            Status.Success(sourcesList)
        } else {
            if (cashedSourcesList.isNullOrEmpty()) {
                validateCachedData(ErrorTypes.NoData)
            } else {
                emitMessage(ErrorModel(message = R.string.something_went_wrong))
                Status.Idle
            }
        }
    }

    // check cashed data if should show error layout or show toast
    private suspend fun validateCachedData(errorType: ErrorTypes): Status<ArrayList<Sources>> {
        return if (cashedSourcesList.isNullOrEmpty()) {
            emitErrorState(true)
            Status.Error(errorType)
        } else {
            errorType.errorTitle?.let { emitMessage(ErrorModel(it)) }
            Status.Idle
        }
    }

    // update cashed data with given new values
    private fun updateCachedNewsList(
        sourcesList: ArrayList<Sources>, forceRefresh: Boolean
    ) {
        when {
            cashedSourcesList == null -> this.cashedSourcesList = sourcesList
            forceRefresh -> {
                this.cashedSourcesList?.clear()
                this.cashedSourcesList?.addAll(sourcesList)
            }
            else -> {
                this.cashedSourcesList?.addAll(sourcesList)
            }
        }
    }

    private fun emitStatus(sourceStatus: Status<ArrayList<Sources>>) {
        _sourcesListingState.value = sourceStatus
    }
}