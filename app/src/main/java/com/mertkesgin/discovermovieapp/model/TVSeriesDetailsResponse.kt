package com.mertkesgin.discovermovieapp.model

import com.google.gson.annotations.SerializedName

data class TVSeriesDetailsResponse(

    @SerializedName("backdrop_path")
    val backdrop_path: String,

    @SerializedName("episode_run_time")
    val episode_run_time: List<Int>,

    @SerializedName("first_air_date")
    val first_air_date: String,

    @SerializedName("genres")
    val genres: List<Genre>,

    @SerializedName("id")
    val id: Int,

    @SerializedName("languages")
    val languages: List<String>,

    @SerializedName("last_air_date")
    val last_air_date: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("original_name")
    val original_name: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("poster_path")
    val poster_path: String,

    @SerializedName("production_companies")
    val production_companies: List<ProductionCompany>,

    @SerializedName("seasons")
    val seasons: List<Season>,

    @SerializedName("status")
    val status: String,

    @SerializedName("vote_average")
    val vote_average: Double
)