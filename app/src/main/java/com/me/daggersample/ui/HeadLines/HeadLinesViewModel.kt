package com.me.daggersample.ui.HeadLines

import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.me.daggersample.R
import com.me.daggersample.base.BaseViewModel
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.headLine.HeadLineModel
import com.me.daggersample.model.networkData.ErrorModel
import com.me.daggersample.source.remote.handler.ResponseStatus
import io.reactivex.Completable
import io.reactivex.Observable

class HeadLinesViewModel(
    private val headLinesRepository: HeadLinesRepository,
    private val sourceId: String?
) : BaseViewModel() {
    private val _headLinesList by lazy { MutableLiveData<ArrayList<HeadLineModel>>() }
    val headLinesList: LiveData<ArrayList<HeadLineModel>>
        get() = _headLinesList

    fun getHeadLines(): Completable {
        return if (sourceId == null) {
            Observable.just(
                ResponseStatus.Error(
                    message = R.string.something_went_wrong, subMessage = R.string.please_try_again
                )
            ).map { mapApiResponse(it) }.ignoreElements()
        } else {
            headLinesRepository.getHeadLinesList(sourceId)
                .doOnSubscribe { handleDoOnSubscribe() }
                .doOnError { handleDoOnError(it) }
                .doOnNext { handleDoOnNext() }
                .map { mapApiResponse(it) }
                .onErrorReturn { handelOnErrorReturn(it) }
                .ignoreElements()
        }
    }

    private fun handleDoOnNext() {
        handleProgressVisibility(GONE)
        _errorLayoutVisibility.postValue(ErrorModel(visibility = GONE))
    }

    // handle progress and error layout visibility
    private fun handleDoOnSubscribe() {
        handleProgressVisibility(VISIBLE)
        _errorLayoutVisibility.postValue(ErrorModel(visibility = GONE))
    }

    // return custom error when throw exception
    private fun handelOnErrorReturn(it: Throwable): ResponseStatus<ApiResponse<ArrayList<HeadLineModel>>> {
        return ResponseStatus.Error(R.string.something_went_wrong, R.string.please_try_again)
    }

    // handle progress and error layout visibility
    private fun handleDoOnError(it: Throwable) {
        handleProgressVisibility(GONE)
        _errorLayoutVisibility.postValue(ErrorModel(visibility = VISIBLE))
    }

    private fun mapApiResponse(it: ResponseStatus<ApiResponse<ArrayList<HeadLineModel>>>): ResponseStatus<ApiResponse<ArrayList<HeadLineModel>>> {
        when (it) {
            is ResponseStatus.Success -> validateHeadLinesList(it.data!!)

            is ResponseStatus.NoNetwork -> onStatusNoNetwork()

            is ResponseStatus.Error -> onStatusError(it)

            is ResponseStatus.ServerError -> onStatusServerError(it)
        }
        return it
    }

    // handle when server respond with error messages
    private fun onStatusServerError(it: ResponseStatus.ServerError) {
        handleProgressVisibility(GONE)
        _errorLayoutVisibility.postValue(
            ErrorModel(serverMessage = it.serverMessage, visibility = VISIBLE)
        )
    }

    // handle when error show error layout
    private fun onStatusError(it: ResponseStatus.Error) {
        _errorLayoutVisibility.value = ErrorModel(
            message = it.message,
            subMessage = it.subMessage,
            visibility = VISIBLE
        )
    }

    // handle when no network show error layout
    private fun onStatusNoNetwork() {
        _errorLayoutVisibility.value = ErrorModel(
            message = R.string.no_network,
            subMessage = R.string.please_try_again,
            visibility = VISIBLE
        )
    }

    // validate data and headlines list
    private fun validateHeadLinesList(data: ApiResponse<ArrayList<HeadLineModel>>) {
        if (data.articles.isNullOrEmpty()) {
            _errorLayoutVisibility.postValue(
                ErrorModel(message = R.string.no_data, visibility = VISIBLE)
            )
        } else {
            _headLinesList.postValue(data.articles)
        }
    }
}