package com.me.daggersample.data

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

data class NewsResponse(
    @SerializedName("News")
    val newsData: ArrayList<NewsModel>
)