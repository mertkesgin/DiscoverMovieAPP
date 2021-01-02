package com.mertkesgin.discovermovieapp.ui.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mertkesgin.discovermovieapp.base.BaseViewModel
import com.mertkesgin.discovermovieapp.model.CastResponse
import com.mertkesgin.discovermovieapp.model.MovieDetailsResponse
import com.mertkesgin.discovermovieapp.model.MovieResponse
import com.mertkesgin.discovermovieapp.model.entry.MovieEntry
import com.mertkesgin.discovermovieapp.repository.MovieDetailsRepository
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val repository: MovieDetailsRepository
) : BaseViewModel(repository) {

    private val _movieDetails: MutableLiveData<Resource<MovieDetailsResponse>> = MutableLiveData()
    val movieDetails : LiveData<Resource<MovieDetailsResponse>>
        get() = _movieDetails

    private val _similarMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val similarMovies: LiveData<Resource<MovieResponse>>
        get() = _similarMovies

    private val _cast: MutableLiveData<Resource<CastResponse>> = MutableLiveData()
    val cast: LiveData<Resource<CastResponse>>
        get() = _cast

    fun gelAllData(movie_id:Int) = viewModelScope.launch {
        _movieDetails.postValue(Resource.Loading)
        coroutineScope {
            val details = async { repository.getMovieDetails(movie_id) }
            val similars = async { repository.getSimilarMovies(movie_id) }
            val cast = async { repository.getMovieCast(movie_id) }

            _movieDetails.value = details.await()
            _similarMovies.value = similars.await()
            _cast.value = cast.await()
        }
    }

    fun insertMovie(movieEntry: MovieEntry) = viewModelScope.launch {
        repository.insertMovie(movieEntry)
    }

    fun deleteMovie(movieEntry: MovieEntry) = viewModelScope.launch {
        repository.deleteMovie(movieEntry)
    }

    fun isMovieExist(movieId:Int) = repository.isMovieExist(movieId)
}