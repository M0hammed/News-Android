package com.me.daggersample.ui.HeadLines.data

import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.base.Status
import com.me.daggersample.model.headLine.HeadLineModel
import java.util.ArrayList
import kotlinx.coroutines.flow.Flow

interface IHeadLinesRepository {

    fun getHeadLinesList(sourceId: String): Flow<Status<ApiResponse<ArrayList<HeadLineModel>>>>
}
