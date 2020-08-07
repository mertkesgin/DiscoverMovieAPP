package com.mertkesgin.discovermovieapp.model

import com.google.gson.annotations.SerializedName
import com.mertkesgin.discovermovieapp.model.entry.CastEntry

data class CastResponse(
    @SerializedName("cast")
    val castEntry: List<CastEntry>,

    @SerializedName("id")
    val id: Int
)