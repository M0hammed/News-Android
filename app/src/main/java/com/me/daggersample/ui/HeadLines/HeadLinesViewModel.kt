package com.me.daggersample.ui.HeadLines

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
    private val _healLinesList by lazy { MutableLiveData<ArrayList<HeadLineModel>>() }
    val healLinesList: LiveData<ArrayList<HeadLineModel>>
        get() = _healLinesList

    fun getHeadLines(): Completable {
        return if (sourceId == null) {
            Observable.just(ResponseStatus.Error())
                .map { mapApiResponse(it) }
                .ignoreElements()
        } else {
            headLinesRepository.getHeadLinesList("sdasdasd")
                .map { mapApiResponse(it) }
                .ignoreElements()
        }
    }

    private fun mapApiResponse(it: ResponseStatus<ApiResponse<ArrayList<HeadLineModel>>>): ResponseStatus<ApiResponse<ArrayList<HeadLineModel>>> {
        when (it) {
            is ResponseStatus.Success -> {
                validateHeadLinesList(it.data)
            }
            is ResponseStatus.NoNetwork -> {
                _errorLayoutVisibility.value = ErrorModel(
                    message = R.string.no_network,
                    subMessage = R.string.please_try_again,
                    visibility = VISIBLE
                )
            }
            is ResponseStatus.Error -> {
                _errorLayoutVisibility.value = ErrorModel(
                    message = R.string.something_went_wrong,
                    subMessage = R.string.please_try_again,
                    visibility = VISIBLE
                )
            }
        }
        return it
    }

    // validate data and headlines list
    private fun validateHeadLinesList(data: ApiResponse<ArrayList<HeadLineModel>>?) {
        if (data != null) {
            if (data.articles != null) {
                if (data.articles!!.isNotEmpty()) {

                } else {
                    _healLinesList.value = data.articles
                    _errorLayoutVisibility.value = ErrorModel(
                        message = R.string.no_data,
                        subMessage = R.string.please_try_again,
                        visibility = VISIBLE
                    )
                }
            } else {

            }
        } else {

        }
    }
}