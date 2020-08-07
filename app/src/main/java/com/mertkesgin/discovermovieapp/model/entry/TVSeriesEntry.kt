package com.mertkesgin.discovermovieapp.model.entry

import com.google.gson.annotations.SerializedName

data class TVSeriesEntry(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("original_name")
    val original_name: String,

    @SerializedName("poster_path")
    val poster_path: String,

    @SerializedName("backdrop_path")
    val backdrop_path: String,

    @SerializedName("vote_average")
    val vote_average: Double
)