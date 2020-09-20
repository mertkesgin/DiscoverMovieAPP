package com.mertkesgin.discovermovieapp.model.entry

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "tv_table")
data class TVSeriesEntry(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val tvSeriesId: Int,

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
): Serializable