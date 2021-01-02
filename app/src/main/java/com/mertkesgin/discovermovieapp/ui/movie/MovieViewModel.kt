package com.mertkesgin.discovermovieapp.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mertkesgin.discovermovieapp.model.MovieResponse
import com.mertkesgin.discovermovieapp.model.PeopleResponse
import com.mertkesgin.discovermovieapp.base.BaseViewModel
import com.mertkesgin.discovermovieapp.repository.MovieRepository
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MovieViewModel(
    private val repository: MovieRepository
) : BaseViewModel(repository){

    private val _trendsOfDayMovie: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val trendsOfDayMovie: LiveData<Resource<MovieResponse>>
        get() = _trendsOfDayMovie

    private val _popularMovies: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val popularMovies: LiveData<Resource<MovieResponse>>
        get() = _popularMovies

    private val _topRated: MutableLiveData<Resource<MovieResponse>> = MutableLiveData()
    val topRated: LiveData<Resource<MovieResponse>>
        get() = _topRated

    private val _popularPeople: MutableLiveData<Resource<PeopleResponse>> = MutableLiveData()
    val popularPeople: LiveData<Resource<PeopleResponse>>
        get() = _popularPeople

    init {
        getAllData()
    }

    private fun getAllData() = viewModelScope.launch{
        _trendsOfDayMovie.value = Resource.Loading
        coroutineScope {
            val trends = async { repository.getTrendsOfDay() }
            val populars = async { repository.getPopularMovies() }
            val topRated = async { repository.getTopRated() }
            val people = async { repository.getPopularPeople() }

            _trendsOfDayMovie.value = trends.await()
            _popularMovies.value = populars.await()
            _topRated.value = topRated.await()
            _popularPeople.value = people.await()
        }
    }
}