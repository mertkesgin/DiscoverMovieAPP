package com.mertkesgin.discovermovieapp.ui.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertkesgin.discovermovieapp.model.CastResponse
import com.mertkesgin.discovermovieapp.model.MovieDetailsResponse
import com.mertkesgin.discovermovieapp.model.MovieResponse
import com.mertkesgin.discovermovieapp.repository.MovieRepository
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response

class MovieDetailsViewModel(
    private val movieRepository: MovieRepository
): ViewModel() {

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
            val details = async { movieRepository.fetchMovieDetails(movie_id) }
            val similars = async { movieRepository.fetchSimilarMovies(movie_id) }
            val cast = async { movieRepository.fetchMovieCast(movie_id) }

            val callDetails = details.await()
            val callSimilars = similars.await()
            val callCast = cast.await()

            _movieDetails.postValue(handleReponse(callDetails))
            _similarMovies.postValue(handleReponse(callSimilars))
            _cast.postValue(handleReponse(callCast))
        }
    }

    private fun <T : Any?> handleReponse(response: Response<T>) : Resource<T>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}