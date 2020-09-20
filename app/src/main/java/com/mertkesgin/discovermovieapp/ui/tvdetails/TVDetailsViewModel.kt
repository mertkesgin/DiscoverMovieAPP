package com.mertkesgin.discovermovieapp.ui.tvdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mertkesgin.discovermovieapp.model.CastResponse
import com.mertkesgin.discovermovieapp.model.TVSeriesDetailsResponse
import com.mertkesgin.discovermovieapp.model.TVSeriesResponse
import com.mertkesgin.discovermovieapp.model.entry.TVSeriesEntry
import com.mertkesgin.discovermovieapp.repository.AppRepository
import com.mertkesgin.discovermovieapp.ui.BaseViewModel
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class TVDetailsViewModel(
    private val appRepository: AppRepository
): BaseViewModel() {

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
        _tvSeriesDetails.postValue(Resource.Loading())
        coroutineScope {
            val details = async { appRepository.fetchTVSeriesDetails(tv_id) }
            val similars = async { appRepository.fetchSimilarTVSeries(tv_id) }
            val cast = async { appRepository.fetchTVSeriesCast(tv_id) }

            _tvSeriesDetails.postValue(getResult { details.await() })
            _similarTVSeries.postValue(getResult { similars.await() })
            _tvCast.postValue(getResult { cast.await() })
        }
    }

    fun insertTvSerie(tvSeriesEntry: TVSeriesEntry) = viewModelScope.launch {
        appRepository.insertTvSeries(tvSeriesEntry)
    }

    fun  isTvSeriesExist(tvSeriesId:Int) = appRepository.isTvSeriesExist(tvSeriesId)

    fun deleteTvSeries(tvSeriesEntry: TVSeriesEntry) = viewModelScope.launch {
        appRepository.deleteTvSeries(tvSeriesEntry)
    }
}