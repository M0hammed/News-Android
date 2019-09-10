package com.me.daggersample.base

import androidx.lifecycle.ViewModel
import com.me.daggersample.data.networkData.ErrorResponse
import io.reactivex.subjects.BehaviorSubject

open class BaseViewModel : ViewModel() {
    val handleProgress = BehaviorSubject.create<Boolean>()
    val handleErrorMessage = BehaviorSubject.create<ErrorResponse>()
}