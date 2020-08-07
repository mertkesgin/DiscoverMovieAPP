package com.mertkesgin.discovermovieapp.model

import com.google.gson.annotations.SerializedName
import com.mertkesgin.discovermovieapp.model.entry.TVSeriesEntry

data class TVSeriesResponse(
    val page: Int,
    @SerializedName("results")
    val tvSeriesEntries: List<TVSeriesEntry>,
    val total_pages: Int,
    val total_results: Int
)