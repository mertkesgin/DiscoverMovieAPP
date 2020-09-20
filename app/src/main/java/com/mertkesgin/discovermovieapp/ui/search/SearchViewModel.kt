package com.mertkesgin.discovermovieapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mertkesgin.discovermovieapp.model.MovieResponse
import com.mertkesgin.discovermovieapp.repository.AppRepository
import com.mertkesgin.discovermovieapp.ui.BaseViewModel
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.launch

class SearchViewModel(
    private val appRepository: AppRepository
): BaseViewModel() {

    private val _movieSearch: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val movieSearch: LiveData<Resource<MovieResponse>>
        get() = _movieSearch

    suspend fun searchMovies(searchQuery: String) = viewModelScope.launch {
        _movieSearch.postValue(Resource.Loading())
        _movieSearch.postValue(getResult { appRepository.fetchResultOfMovieSearch(searchQuery) })
    }
}