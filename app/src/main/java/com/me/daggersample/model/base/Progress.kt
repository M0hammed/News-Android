package com.me.daggersample.model.base

sealed class Progress(open val isLoading: Boolean) {
    data class Main(override val isLoading: Boolean) : Progress(isLoading)
    data class Refresh(override val isLoading: Boolean) : Progress(isLoading)
    data class Paging(override val isLoading: Boolean) : Progress(isLoading)
}
