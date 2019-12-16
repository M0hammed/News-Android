package com.me.daggersample.model.team

import com.google.gson.annotations.SerializedName

data class Teams (
    @SerializedName("team_flag")
    val teamFlag: String,
    @SerializedName("rounded_flag")
    val roundedFlag: String? = null,
    @SerializedName("team_name")
    val teamName: String?,
    @SerializedName("team_nid")
    val teamNid: String? = null ?: ""
)