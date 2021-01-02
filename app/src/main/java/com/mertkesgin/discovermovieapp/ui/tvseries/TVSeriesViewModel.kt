package com.mertkesgin.discovermovieapp.ui.tvseries

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mertkesgin.discovermovieapp.model.TVSeriesResponse
import com.mertkesgin.discovermovieapp.base.BaseViewModel
import com.mertkesgin.discovermovieapp.repository.TvSeriesRepository
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class TVSeriesViewModel(
    private val repository: TvSeriesRepository
): BaseViewModel(repository) {

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
        _trendsOfDayTV.value = Resource.Loading
        coroutineScope {
            val trends = async { repository.getTrendsOfTv() }
            val populars = async { repository.getPopularTvSeries() }
            val topRated = async { repository.getTopRatedTVSeries() }

            _trendsOfDayTV.value = trends.await()
            _popularTVSeries.value = populars.await()
            _topRated.value = topRated.await()
        }
    }
}