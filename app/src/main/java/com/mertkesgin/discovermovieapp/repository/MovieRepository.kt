package com.mertkesgin.discovermovieapp.repository

import com.mertkesgin.discovermovieapp.base.BaseRepository
import com.mertkesgin.discovermovieapp.data.local.AppDatabase
import com.mertkesgin.discovermovieapp.data.remote.MovieApi
import com.mertkesgin.discovermovieapp.data.remote.PeopleApi

class MovieRepository(
    private val movieApi: MovieApi,
    private val peopleApi: PeopleApi
) : BaseRepository() {

    suspend fun getTrendsOfDay() = safeApiCall { movieApi.getTrendsOfDay() }

    suspend fun getPopularMovies() = safeApiCall { movieApi.getPopularMovies() }

    suspend fun getTopRated() = safeApiCall { movieApi.getTopRated() }

    suspend fun getPopularPeople() = safeApiCall { peopleApi.getPopularPeople() }
}