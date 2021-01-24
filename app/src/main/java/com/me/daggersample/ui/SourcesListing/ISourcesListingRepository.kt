package com.me.daggersample.ui.SourcesListing

import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.source.Sources
import com.me.daggersample.source.remote.handler.Status
import kotlinx.coroutines.flow.Flow

interface ISourcesListingRepository {
    fun printMessage()
    fun getNews(): Flow<Status<ApiResponse<ArrayList<Sources>>>>
}