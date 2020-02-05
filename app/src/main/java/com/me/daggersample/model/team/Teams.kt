package com.me.daggersample.model.team

import com.google.gson.annotations.SerializedName

data class Teams(
    @SerializedName("team_flag")
    val teamFlag: String? = null,
    @SerializedName("rounded_flag")
    val roundedFlag: String? = null,
    @SerializedName("team_name")
    val teamName: String? = null,
    @SerializedName("team_nid")
    val teamNid: String? = null
)