package com.mertkesgin.discovermovieapp.data.remote

import com.mertkesgin.discovermovieapp.model.TVSeriesDetailsResponse
import com.mertkesgin.discovermovieapp.model.TVSeriesResponse
import com.mertkesgin.discovermovieapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvSeriesApi {

    @GET("trending/tv/day")
    suspend fun getTrendsOfDayTV(
        @Query("api_key") api_key: String = Constants.API_KEY,
        @Query("language") language: String = Constants.LANGUAGE_EN
    ): TVSeriesResponse

    @GET("tv/popular")
    suspend fun getPopularTVSeries(
        @Query("api_key") api_key: String = Constants.API_KEY
    ): TVSeriesResponse

    @GET("tv/70523/recommendations")
    suspend fun getTopRatedTV(
        @Query("api_key") api_key: String = Constants.API_KEY,
        @Query("language") language: String = Constants.LANGUAGE_EN
    ): TVSeriesResponse

    @GET("tv/{tv_id}")
    suspend fun getTVSeriesDetails(
        @Path("tv_id") tv_id:Int,
        @Query("api_key") api_key: String = Constants.API_KEY,
        @Query("language") language: String = Constants.LANGUAGE_TR
    ): TVSeriesDetailsResponse

    @GET("tv/{tv_id}/similar")
    suspend fun getSimilarTVSeries(
        @Path("tv_id") tv_id: Int,
        @Query("api_key") api_key: String = Constants.API_KEY
    ): TVSeriesResponse
}