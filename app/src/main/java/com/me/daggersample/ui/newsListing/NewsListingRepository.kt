package com.me.daggersample.ui.newsListing

import com.me.daggersample.base.BaseRepository
import com.me.daggersample.model.base.ApiResponse
import com.me.daggersample.model.team.Teams
import com.me.daggersample.source.remote.apiInterface.RetrofitApisInterface
import com.me.daggersample.source.remote.data_source.IRemoteDataSource
import com.me.daggersample.source.remote.data_source.RemoteDataSource
import com.me.daggersample.source.remote.handler.ResponseStatus
import com.me.daggersample.source.remote.handler.getNetworkResponse
import io.reactivex.Observable
import javax.inject.Inject

class NewsListingRepository @Inject constructor(val iRemoteDataSource: IRemoteDataSource) :
    BaseRepository() {

    fun getListingNews(): Observable<ResponseStatus<ApiResponse<ArrayList<Teams>>>> {
        return iRemoteDataSource.getTeamsList()
    }

    override fun cancelApiCall() {
        iRemoteDataSource.cancelApiCall("teamsTag")
    }
}