package com.mertkesgin.discovermovieapp.ui.tvdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mertkesgin.discovermovieapp.model.CastResponse
import com.mertkesgin.discovermovieapp.model.TVSeriesDetailsResponse
import com.mertkesgin.discovermovieapp.model.TVSeriesResponse
import com.mertkesgin.discovermovieapp.model.entry.TVSeriesEntry
import com.mertkesgin.discovermovieapp.base.BaseViewModel
import com.mertkesgin.discovermovieapp.repository.TvSeriesDetailsRepository
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class TVDetailsViewModel(
    private val repository: TvSeriesDetailsRepository
): BaseViewModel(repository) {

    private val _tvSeriesDetails:MutableLiveData<Resource<TVSeriesDetailsResponse>> = MutableLiveData()
    val tvSeriesDetails: LiveData<Resource<TVSeriesDetailsResponse>>
        get() = _tvSeriesDetails

    private val _similarTVSeries:MutableLiveData<Resource<TVSeriesResponse>> = MutableLiveData()
    val similarTVSeries: LiveData<Resource<TVSeriesResponse>>
        get() = _similarTVSeries

    private val _tvCast: MutableLiveData<Resource<CastResponse>> = MutableLiveData()
    val tvCast: LiveData<Resource<CastResponse>>
        get() = _tvCast

    fun getAllData(tv_id:Int) = viewModelScope.launch {
        _tvSeriesDetails.postValue(Resource.Loading)
        coroutineScope {
            val details = async { repository.getTvSeriesDetails(tv_id) }
            val similars = async { repository.getSimilarTvSeries(tv_id) }
            val cast = async { repository.getTvSeriesCast(tv_id) }

            _tvSeriesDetails.value = details.await()
            _similarTVSeries.value = similars.await()
            _tvCast.value = cast.await()
        }
    }

    fun insertTvSeries(tvSeriesEntry: TVSeriesEntry) = viewModelScope.launch {
        repository.insertTvSeries(tvSeriesEntry)
    }

    fun  isTvSeriesExist(tvSeriesId:Int) = repository.isTvSeriesExist(tvSeriesId)

    fun deleteTvSeries(tvSeriesEntry: TVSeriesEntry) = viewModelScope.launch {
        repository.deleteTvSeries(tvSeriesEntry)
    }
}