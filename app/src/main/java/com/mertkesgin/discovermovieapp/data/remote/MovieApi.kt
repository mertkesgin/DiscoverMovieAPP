package com.mertkesgin.discovermovieapp.data.remote

import com.mertkesgin.discovermovieapp.model.MovieDetailsResponse
import com.mertkesgin.discovermovieapp.model.MovieResponse
import com.mertkesgin.discovermovieapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("trending/movie/day")
    suspend fun getTrendsOfDay(
        @Query("api_key") api_key:String = Constants.API_KEY
    ) : MovieResponse

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") api_key: String = Constants.API_KEY,
        @Query("language") language: String = Constants.LANGUAGE_EN
    ): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("api_key") api_key: String = Constants.API_KEY
    ): MovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = Constants.API_KEY,
        @Query("language") language: String = Constants.LANGUAGE_TR
    ): MovieDetailsResponse

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = Constants.API_KEY
    ): MovieResponse

    @GET("search/movie")
    suspend fun getResultOfMovieSearch(
        @Query("query") searchQuery:String,
        @Query("api_key") api_key: String = Constants.API_KEY
    ): MovieResponse
}