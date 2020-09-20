package com.mertkesgin.discovermovieapp.model.entry

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "movie_table")
data class MovieEntry(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    val movieId: Int,

    @SerializedName("original_title")
    val original_title: String,

    @SerializedName("poster_path")
    val poster_path: String,

    @SerializedName("backdrop_path")
    val backdrop_path: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("vote_average")
    val vote_average: Double
):Serializable