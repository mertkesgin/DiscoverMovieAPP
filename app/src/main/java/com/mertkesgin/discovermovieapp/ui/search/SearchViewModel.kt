package com.mertkesgin.discovermovieapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mertkesgin.discovermovieapp.model.MovieResponse
import com.mertkesgin.discovermovieapp.base.BaseViewModel
import com.mertkesgin.discovermovieapp.repository.SearchRepository
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: SearchRepository
) : BaseViewModel(repository) {

    private val _movieSearch: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val movieSearch: LiveData<Resource<MovieResponse>>
        get() = _movieSearch

    suspend fun searchMovies(searchQuery: String) = viewModelScope.launch {
        _movieSearch.postValue(Resource.Loading)
        _movieSearch.value = repository.searchMovie(searchQuery)
    }
}