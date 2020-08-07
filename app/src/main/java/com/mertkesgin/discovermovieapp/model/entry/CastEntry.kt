package com.mertkesgin.discovermovieapp.model.entry

import com.google.gson.annotations.SerializedName

data class CastEntry(

    @SerializedName("character")
    val character: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("order")
    val order: Int,

    @SerializedName("profile_path")
    val profile_path: String
)