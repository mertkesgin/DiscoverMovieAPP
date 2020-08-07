package com.mertkesgin.discovermovieapp.model

import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(

    @SerializedName("backdrop_path")
    val backdrop_path: String,

    @SerializedName("genres")
    val genres: List<Genre>,

    @SerializedName("id")
    val id: Int,

    @SerializedName("original_title")
    val original_title: String,

    @SerializedName("overview")
    val overview: String,

    @SerializedName("poster_path")
    val poster_path: String,

    @SerializedName("production_companies")
    val production_companies: List<ProductionCompany>,

    @SerializedName("release_date")
    val release_date: String,

    @SerializedName("runtime")
    val runtime: Int,

    @SerializedName("vote_average")
    val vote_average: Double
)