package com.mertkesgin.discovermovieapp.repository


import com.mertkesgin.discovermovieapp.data.remote.RetrofitInstance


class MovieRepository(

) {

    //MOVIE
    suspend fun fetchTrendsOfDayMovie() = RetrofitInstance.api.getTrendsOfDayMovie()

    suspend fun fetchPopularMovies() = RetrofitInstance.api.getPopularMovies()

    suspend fun fetchTopRatedMovies() = RetrofitInstance.api.getTopRated()

    suspend fun fetchMovieDetails(movie_id:Int) = RetrofitInstance.api.getMovieDetails(movie_id)

    suspend fun fetchSimilarMovies(movie_id: Int) = RetrofitInstance.api.getSimilarMovies(movie_id)

    suspend fun fetchMovieCast(movie_id: Int) = RetrofitInstance.api.getMovieCast(movie_id)

    suspend fun fetchResultOfMovieSearch(searchQuery:String) = RetrofitInstance.api.getResultOfMovieSearch(searchQuery)

    //TV SERIES
    suspend fun fetchTrendsOfDayTV() = RetrofitInstance.api.getTrendsOfDayTV()

    suspend fun fetchPopularTVSeries() = RetrofitInstance.api.getPopularTVSeries()

    suspend fun fetchTopRatedTVSeries() = RetrofitInstance.api.getTopRatedTV()

    suspend fun fetchTVSeriesDetails(tv_id:Int) = RetrofitInstance.api.getTVSeriesDetails(tv_id)

    suspend fun fetchSimilarTVSeries(tv_id: Int) = RetrofitInstance.api.getSimilarTVSeries(tv_id)

    suspend fun fetchTVSeriesCast(tv_id: Int) = RetrofitInstance.api.getTVCast(tv_id)

    suspend fun fetchPopularPeople() = RetrofitInstance.api.getPopularPeople()

}