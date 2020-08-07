package com.mertkesgin.discovermovieapp.model.entry

import com.google.gson.annotations.SerializedName


data class PeopleEntry(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("profile_path")
    val profile_path: String
)