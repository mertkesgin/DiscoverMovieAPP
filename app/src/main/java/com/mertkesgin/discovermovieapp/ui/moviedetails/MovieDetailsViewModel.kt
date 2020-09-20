package com.mertkesgin.discovermovieapp.ui.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mertkesgin.discovermovieapp.model.CastResponse
import com.mertkesgin.discovermovieapp.model.MovieDetailsResponse
import com.mertkesgin.discovermovieapp.model.MovieResponse
import com.mertkesgin.discovermovieapp.model.entry.MovieEntry
import com.mertkesgin.discovermovieapp.repository.AppRepository
import com.mertkesgin.discovermovieapp.ui.BaseViewModel
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
    private val appRepository: AppRepository
): BaseViewModel() {

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
        _movieDetails.postValue(Resource.Loading())
        coroutineScope {
            val details = async { appRepository.fetchMovieDetails(movie_id) }
            val similars = async { appRepository.fetchSimilarMovies(movie_id) }
            val cast = async { appRepository.fetchMovieCast(movie_id) }

            _movieDetails.postValue(getResult { details.await() })
            _similarMovies.postValue(getResult { similars.await() })
            _cast.postValue(getResult { cast.await() })
        }
    }

    fun insertMovie(movieEntry: MovieEntry) = viewModelScope.launch {
        appRepository.insertMovie(movieEntry)
    }

    fun isMovieExist(movieId:Int) = appRepository.isMovieExist(movieId)

    fun deleteMovie(movieEntry: MovieEntry) = viewModelScope.launch {
        appRepository.deleteMovie(movieEntry)
    }
}