package com.me.daggersample.source.remote.apiInterface

import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.source.Sources
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitApisInterface {
    @GET(ConstantsKeys.ApiKeys.SOURCES)
    fun getTeams(): Call<ApiResponse<ArrayList<Sources>>>
}