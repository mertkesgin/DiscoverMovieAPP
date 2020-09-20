package com.mertkesgin.discovermovieapp.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mertkesgin.discovermovieapp.repository.AppRepository
import com.mertkesgin.discovermovieapp.model.MovieResponse
import com.mertkesgin.discovermovieapp.model.PeopleResponse
import com.mertkesgin.discovermovieapp.ui.BaseViewModel
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MovieViewModel(
    private val appRepository: AppRepository
) : BaseViewModel(){

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
        _trendsOfDayMovie.postValue(Resource.Loading())
        coroutineScope {
            val trends = async { appRepository.fetchTrendsOfDayMovie() }
            val populars = async { appRepository.fetchPopularMovies() }
            val topRateds = async { appRepository.fetchTopRatedMovies() }
            val people = async { appRepository.fetchPopularPeople() }

            _trendsOfDayMovie.postValue(getResult { trends.await() })
            _popularMovies.postValue(getResult { populars.await() })
            _topRated.postValue(getResult { topRateds.await() })
            _popularPeople.postValue(getResult { people.await() })
        }
    }
}