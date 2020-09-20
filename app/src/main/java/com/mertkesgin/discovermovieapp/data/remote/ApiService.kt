package com.mertkesgin.discovermovieapp.data.remote

import com.mertkesgin.discovermovieapp.model.*
import com.mertkesgin.discovermovieapp.utils.Constants.API_KEY
import com.mertkesgin.discovermovieapp.utils.Constants.LANGUAGE_EN
import com.mertkesgin.discovermovieapp.utils.Constants.LANGUAGE_TR

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // MOVEI API RESPONSE
    @GET("trending/movie/day")
    suspend fun getTrendsOfDayMovie(
        @Query("api_key") api_key:String = API_KEY
    ) : Response<MovieResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String = LANGUAGE_EN
    ): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("api_key") api_key: String = API_KEY
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String = LANGUAGE_TR
    ): Response<MovieDetailsResponse>

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = API_KEY
    ): Response<MovieResponse>

    @GET("search/movie")
    suspend fun getResultOfMovieSearch(
        @Query("query") searchQuery:String,
        @Query("api_key") api_key: String = API_KEY
    ): Response<MovieResponse>

    // TV SERIES API RESPONSE
    @GET("trending/tv/day")
    suspend fun getTrendsOfDayTV(
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String = LANGUAGE_EN
    ): Response<TVSeriesResponse>

    @GET("tv/popular")
    suspend fun getPopularTVSeries(
        @Query("api_key") api_key: String = API_KEY
    ): Response<TVSeriesResponse>

    @GET("tv/70523/recommendations")
    suspend fun getTopRatedTV(
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String = LANGUAGE_EN
    ): Response<TVSeriesResponse>

    @GET("tv/{tv_id}")
    suspend fun getTVSeriesDetails(
        @Path("tv_id") tv_id:Int,
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String = LANGUAGE_TR
    ): Response<TVSeriesDetailsResponse>

    @GET("tv/{tv_id}/similar")
    suspend fun getSimilarTVSeries(
        @Path("tv_id") tv_id: Int,
        @Query("api_key") api_key: String = API_KEY
    ): Response<TVSeriesResponse>

    // PEOPLE API RESPONSE
    @GET("person/popular")
    suspend fun getPopularPeople(
        @Query("api_key") api_key: String = API_KEY,
        @Query("language") language: String = LANGUAGE_EN
    ): Response<PeopleResponse>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCast(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = API_KEY
    ): Response<CastResponse>

    @GET("tv/{tv_id}/credits")
    suspend fun getTVCast(
        @Path("tv_id") movie_id: Int,
        @Query("api_key") api_key: String = API_KEY
    ): Response<CastResponse>
}