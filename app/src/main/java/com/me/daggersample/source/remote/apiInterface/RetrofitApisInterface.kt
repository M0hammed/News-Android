package com.me.daggersample.source.remote.apiInterface

import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.headLine.HeadLineModel
import com.me.daggersample.model.source.Sources
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApisInterface {
    @GET(ConstantsKeys.ApiKeys.SOURCES)
    fun getNews(): Call<ApiResponse<ArrayList<Sources>>>

    @GET(ConstantsKeys.ApiKeys.HEADLINE)
    fun getHeadLineList(@Query(ConstantsKeys.ApiKeys.SOURCES) sourcesId: String):
        Call<ApiResponse<ArrayList<HeadLineModel>>>
}
