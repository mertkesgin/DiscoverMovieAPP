package com.mertkesgin.discovermovieapp.ui.tvseries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertkesgin.discovermovieapp.model.MovieResponse
import com.mertkesgin.discovermovieapp.model.TVSeriesResponse
import com.mertkesgin.discovermovieapp.repository.MovieRepository
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response

class TVSeriesViewModel(
    private val movieRepository: MovieRepository
): ViewModel() {

    private val _trendsOfDayTV: MutableLiveData<Resource<TVSeriesResponse>> = MutableLiveData()
    val trendsOfDayTV: LiveData<Resource<TVSeriesResponse>>
        get() = _trendsOfDayTV

    private val _popularTVSeries: MutableLiveData<Resource<TVSeriesResponse>> = MutableLiveData()
    val popularTVSeries: LiveData<Resource<TVSeriesResponse>>
        get() = _popularTVSeries

    private val _topRated: MutableLiveData<Resource<TVSeriesResponse>> = MutableLiveData()
    val topRated: LiveData<Resource<TVSeriesResponse>>
        get() = _topRated

    init {
        getAllData()
    }

    private fun getAllData() = viewModelScope.launch {
        _trendsOfDayTV.postValue(Resource.Loading())
        coroutineScope {
            val trends = async { movieRepository.fetchTrendsOfDayTV() }
            val populars = async { movieRepository.fetchPopularTVSeries() }
            val topRateds = async { movieRepository.fetchTopRatedTVSeries() }

            val callTrends = trends.await()
            val callPopular = populars.await()
            val callTopRateds = topRateds.await()

            _trendsOfDayTV.postValue(handleResponse(callTrends))
            _popularTVSeries.postValue(handleResponse(callPopular))
            _topRated.postValue(handleResponse(callTopRateds))
        }
    }

    private fun <T : Any?> handleResponse(response: Response<T>) : Resource<T>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}