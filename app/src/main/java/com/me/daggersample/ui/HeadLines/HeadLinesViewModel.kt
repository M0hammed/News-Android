package com.me.daggersample.ui.HeadLines

import com.me.daggersample.base.BaseViewModel

class HeadLinesViewModel(
    private val headLinesRepository: HeadLinesRepository,
    private val sourceId: String?
) : BaseViewModel() {
}