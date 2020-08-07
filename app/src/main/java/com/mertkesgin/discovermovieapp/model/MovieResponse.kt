package com.mertkesgin.discovermovieapp.model

import com.google.gson.annotations.SerializedName
import com.mertkesgin.discovermovieapp.model.entry.MovieEntry

data class MovieResponse(

    val page: Int,
    @SerializedName("results")
    val movieEntries: List<MovieEntry>,
    val total_pages: Int,
    val total_results: Int
)