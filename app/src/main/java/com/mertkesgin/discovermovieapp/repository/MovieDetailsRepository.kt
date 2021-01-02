package com.mertkesgin.discovermovieapp.repository

import com.mertkesgin.discovermovieapp.base.BaseRepository
import com.mertkesgin.discovermovieapp.data.local.AppDatabase
import com.mertkesgin.discovermovieapp.data.remote.MovieApi
import com.mertkesgin.discovermovieapp.data.remote.PeopleApi
import com.mertkesgin.discovermovieapp.model.entry.MovieEntry

class MovieDetailsRepository(
    private val movieApi: MovieApi,
    private val peopleApi: PeopleApi,
    private val database: AppDatabase
) : BaseRepository() {

    suspend fun getMovieDetails(id:Int) = safeApiCall{ movieApi.getMovieDetails(id) }

    suspend fun getSimilarMovies(id: Int) = safeApiCall{ movieApi.getSimilarMovies(id) }

    suspend fun getMovieCast(id: Int) = safeApiCall{ peopleApi.getMovieCast(id) }

    suspend fun insertMovie(movieEntry: MovieEntry) = database.getMovieDao().insertMovie(movieEntry)

    suspend fun deleteMovie(movieEntry: MovieEntry) = database.getMovieDao().deleteMovie(movieEntry)

    fun isMovieExist(movieId:Int) = database.getMovieDao().isMovieExist(movieId)
}