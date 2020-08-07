package com.mertkesgin.discovermovieapp.ui.tvdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mertkesgin.discovermovieapp.model.CastResponse
import com.mertkesgin.discovermovieapp.model.TVSeriesDetailsResponse
import com.mertkesgin.discovermovieapp.model.TVSeriesResponse
import com.mertkesgin.discovermovieapp.model.entry.CastEntry
import com.mertkesgin.discovermovieapp.model.entry.TVSeriesEntry
import com.mertkesgin.discovermovieapp.repository.MovieRepository
import com.mertkesgin.discovermovieapp.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response

class TVDetailsViewModel(
    private val movieRepository: MovieRepository
): ViewModel() {

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
            val details = async { movieRepository.fetchTVSeriesDetails(tv_id) }
            val similars = async { movieRepository.fetchSimilarTVSeries(tv_id) }
            val cast = async { movieRepository.fetchTVSeriesCast(tv_id) }

            val callDetails = details.await()
            val callSimilars = similars.await()
            val callTVCast = cast.await()

            _tvSeriesDetails.postValue(handleReponse(callDetails))
            _similarTVSeries.postValue(handleReponse(callSimilars))
            _tvCast.postValue(handleReponse(callTVCast))
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