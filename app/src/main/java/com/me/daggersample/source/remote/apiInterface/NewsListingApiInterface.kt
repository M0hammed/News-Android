package com.me.daggersample.source.remote.apiInterface

import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.team.Teams
import retrofit2.Call
import retrofit2.http.GET

interface NewsListingApiInterface {
    @GET("competitions/54/teams")
    fun getTeams():Call<ApiResponse<ArrayList<Teams>>>
}