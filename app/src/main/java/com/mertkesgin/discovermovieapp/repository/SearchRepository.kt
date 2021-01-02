package com.mertkesgin.discovermovieapp.repository

import com.mertkesgin.discovermovieapp.base.BaseRepository
import com.mertkesgin.discovermovieapp.data.remote.MovieApi

class SearchRepository(
    private val api: MovieApi
) : BaseRepository() {

    suspend fun searchMovie(searchQuery: String) = safeApiCall { api.getResultOfMovieSearch(searchQuery) }
}