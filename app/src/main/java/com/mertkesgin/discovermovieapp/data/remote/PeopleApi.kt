package com.mertkesgin.discovermovieapp.data.remote

import com.mertkesgin.discovermovieapp.model.CastResponse
import com.mertkesgin.discovermovieapp.model.PeopleResponse
import com.mertkesgin.discovermovieapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeopleApi {

    @GET("person/popular")
    suspend fun getPopularPeople(
        @Query("api_key") api_key: String = Constants.API_KEY,
        @Query("language") language: String = Constants.LANGUAGE_EN
    ): PeopleResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCast(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String = Constants.API_KEY
    ): CastResponse

    @GET("tv/{tv_id}/credits")
    suspend fun getTVCast(
        @Path("tv_id") tv_id: Int,
        @Query("api_key") api_key: String = Constants.API_KEY
    ): CastResponse
}