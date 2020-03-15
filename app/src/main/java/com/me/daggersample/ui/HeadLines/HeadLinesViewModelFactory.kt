package com.me.daggersample.ui.HeadLines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HeadLinesViewModelFactory(
    private val headLinesRepository: HeadLinesRepository,
    private val sourceId: String?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HeadLinesViewModel(headLinesRepository, sourceId) as T
    }
}