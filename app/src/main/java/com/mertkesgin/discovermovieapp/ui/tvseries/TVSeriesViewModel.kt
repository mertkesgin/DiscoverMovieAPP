package com.mertkesgin.discovermovieapp.ui.tvseries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mertkesgin.discovermovieapp.model.TVSeriesResponse
import com.mertkesgin.discovermovieapp.repository.AppRepository
import com.mertkesgin.discovermovieapp.ui.BaseViewModel
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class TVSeriesViewModel(
    private val appRepository: AppRepository
): BaseViewModel() {

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
            val trends = async { appRepository.fetchTrendsOfDayTV() }
            val populars = async { appRepository.fetchPopularTVSeries() }
            val topRateds = async { appRepository.fetchTopRatedTVSeries() }

            _trendsOfDayTV.postValue(getResult { trends.await() })
            _popularTVSeries.postValue(getResult { populars.await() })
            _topRated.postValue(getResult { topRateds.await() })
        }
    }
}