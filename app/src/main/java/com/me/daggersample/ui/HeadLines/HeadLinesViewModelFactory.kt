package com.me.daggersample.ui.HeadLines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HeadLinesViewModelFactory(private val headLinesRepository: HeadLinesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HeadLinesViewModel(headLinesRepository) as T
    }
}