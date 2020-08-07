package com.mertkesgin.discovermovieapp.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertkesgin.discovermovieapp.repository.MovieRepository
import com.mertkesgin.discovermovieapp.model.MovieResponse
import com.mertkesgin.discovermovieapp.model.PeopleResponse
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response

class MovieViewModel(
    private val movieRepository: MovieRepository
) : ViewModel(){

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
            val trends = async { movieRepository.fetchTrendsOfDayMovie() }
            val populars = async { movieRepository.fetchPopularMovies() }
            val topRateds = async { movieRepository.fetchTopRatedMovies() }
            val people = async { movieRepository.fetchPopularPeople() }

            val callTrends = trends.await()
            val callPopulars = populars.await()
            val callTopRateds = topRateds.await()
            val callPeople = people.await()

            _trendsOfDayMovie.postValue(handleReponse(callTrends))
            _popularMovies.postValue(handleReponse(callPopulars))
            _topRated.postValue(handleReponse(callTopRateds))
            _popularPeople.postValue(handleReponse(callPeople))
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