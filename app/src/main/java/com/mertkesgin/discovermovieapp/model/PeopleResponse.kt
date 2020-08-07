package com.mertkesgin.discovermovieapp.model

import com.google.gson.annotations.SerializedName
import com.mertkesgin.discovermovieapp.model.entry.PeopleEntry

data class PeopleResponse(
    val page: Int,
    @SerializedName("results")
    val peopleEntries: List<PeopleEntry>,
    val total_pages: Int,
    val total_results: Int
)