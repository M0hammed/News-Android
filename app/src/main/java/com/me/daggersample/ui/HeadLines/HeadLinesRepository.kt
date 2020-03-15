package com.me.daggersample.ui.HeadLines

import com.me.daggersample.base.BaseRepository
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.validator.INetworkValidator

class HeadLinesRepository(
    private val iRemoteDataSource: IRemoteDataSource,
    private val networkValidator: INetworkValidator
) : BaseRepository() {
    override fun cancelApiCall() {

    }
}