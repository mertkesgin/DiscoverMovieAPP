package com.mertkesgin.discovermovieapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertkesgin.discovermovieapp.model.MovieResponse
import com.mertkesgin.discovermovieapp.repository.MovieRepository
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchViewModel(
    private val movieRepository: MovieRepository
): ViewModel() {

    private val _movieSearch: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val movieSearch: LiveData<Resource<MovieResponse>>
        get() = _movieSearch

    suspend fun searchMovies(searchQuery: String) = viewModelScope.launch {
        _movieSearch.postValue(Resource.Loading())
        val response = movieRepository.fetchResultOfMovieSearch(searchQuery)
        _movieSearch.postValue(handleReponse(response))
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