package com.mertkesgin.discovermovieapp.repository


import com.mertkesgin.discovermovieapp.data.local.AppDatabase
import com.mertkesgin.discovermovieapp.data.remote.RetrofitInstance
import com.mertkesgin.discovermovieapp.model.entry.MovieEntry
import com.mertkesgin.discovermovieapp.model.entry.TVSeriesEntry


class AppRepository(
    val appDatabase: AppDatabase
) {

    //MOVIE
    suspend fun fetchTrendsOfDayMovie() = RetrofitInstance.api.getTrendsOfDayMovie()

    suspend fun fetchPopularMovies() = RetrofitInstance.api.getPopularMovies()

    suspend fun fetchTopRatedMovies() = RetrofitInstance.api.getTopRated()

    suspend fun fetchMovieDetails(movie_id:Int) = RetrofitInstance.api.getMovieDetails(movie_id)

    suspend fun fetchSimilarMovies(movie_id: Int) = RetrofitInstance.api.getSimilarMovies(movie_id)

    suspend fun fetchMovieCast(movie_id: Int) = RetrofitInstance.api.getMovieCast(movie_id)

    suspend fun fetchResultOfMovieSearch(searchQuery:String) = RetrofitInstance.api.getResultOfMovieSearch(searchQuery)

    suspend fun insertMovie(movieEntry: MovieEntry) = appDatabase.getMovieDao().insertMovie(movieEntry)

    suspend fun deleteMovie(movieEntry: MovieEntry) = appDatabase.getMovieDao().deleteMovie(movieEntry)

    fun getMovieList() = appDatabase.getMovieDao().getMovieList()

    fun isMovieExist(movieId: Int) = appDatabase.getMovieDao().isMovieExist(movieId)

    //TV SERIES
    suspend fun fetchTrendsOfDayTV() = RetrofitInstance.api.getTrendsOfDayTV()

    suspend fun fetchPopularTVSeries() = RetrofitInstance.api.getPopularTVSeries()

    suspend fun fetchTopRatedTVSeries() = RetrofitInstance.api.getTopRatedTV()

    suspend fun fetchTVSeriesDetails(tv_id:Int) = RetrofitInstance.api.getTVSeriesDetails(tv_id)

    suspend fun fetchSimilarTVSeries(tv_id: Int) = RetrofitInstance.api.getSimilarTVSeries(tv_id)

    suspend fun fetchTVSeriesCast(tv_id: Int) = RetrofitInstance.api.getTVCast(tv_id)

    suspend fun fetchPopularPeople() = RetrofitInstance.api.getPopularPeople()

    suspend fun insertTvSeries(tvSeriesEntry: TVSeriesEntry) = appDatabase.getTvSeriesDao().insertTvSerie(tvSeriesEntry)

    suspend fun deleteTvSeries(tvSeriesEntry: TVSeriesEntry) = appDatabase.getTvSeriesDao().deleteTvSerie(tvSeriesEntry)

    fun getTvList() = appDatabase.getTvSeriesDao().getTvList()

    fun isTvSeriesExist(tvSeriesId:Int) = appDatabase.getTvSeriesDao().isTvSeriesExist(tvSeriesId)
}