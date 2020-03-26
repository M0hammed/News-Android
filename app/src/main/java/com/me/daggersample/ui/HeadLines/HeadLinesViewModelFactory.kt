package com.me.daggersample.ui.HeadLines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.me.daggersample.source.remote.apiInterface.ConstantsKeys
import javax.inject.Inject
import javax.inject.Named

class HeadLinesViewModelFactory @Inject constructor(
    private val headLinesRepository: HeadLinesRepository,
    @Named(ConstantsKeys.DaggerNames.SOURCE_NAME) private val sourceId: String?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HeadLinesViewModel(headLinesRepository, sourceId) as T
    }
}