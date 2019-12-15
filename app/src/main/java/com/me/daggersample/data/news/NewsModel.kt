package com.me.daggersample.data.news

import com.google.gson.annotations.SerializedName

data class NewsModel(
    @SerializedName("NewsTitle")
    val newsTitle: String = "",
    @SerializedName("ItemDescription")
    val itemDescription: String = "",
    @SerializedName("Nid")
    val nId: String = "",
    @SerializedName("PostDate")
    val postDate: String = "",
    @SerializedName("ImageUrl")
    val imageUrl: String = "",
    @SerializedName("NewsType")
    val newsType: String = "",
    @SerializedName("NumofViews")
    val numofViews: String = "",
    @SerializedName("Likes")
    val likes: String = ""
)