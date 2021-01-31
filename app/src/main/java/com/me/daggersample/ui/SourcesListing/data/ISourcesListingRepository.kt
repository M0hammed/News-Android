package com.me.daggersample.ui.SourcesListing.data

import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.source.Sources
import com.me.daggersample.model.base.Status
import kotlinx.coroutines.flow.Flow

interface ISourcesListingRepository {
    fun getNews(): Flow<Status<ApiResponse<ArrayList<Sources>>>>
}