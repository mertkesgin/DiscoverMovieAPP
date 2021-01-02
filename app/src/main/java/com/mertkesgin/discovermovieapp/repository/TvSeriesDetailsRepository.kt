package com.mertkesgin.discovermovieapp.repository

import com.mertkesgin.discovermovieapp.base.BaseRepository
import com.mertkesgin.discovermovieapp.data.local.AppDatabase
import com.mertkesgin.discovermovieapp.data.remote.PeopleApi
import com.mertkesgin.discovermovieapp.data.remote.TvSeriesApi
import com.mertkesgin.discovermovieapp.model.entry.TVSeriesEntry

class TvSeriesDetailsRepository(
    private val tvSeriesApi: TvSeriesApi,
    private val peopleApi: PeopleApi,
    private val database: AppDatabase
) : BaseRepository() {

    suspend fun getTvSeriesDetails(id:Int) = safeApiCall { tvSeriesApi.getTVSeriesDetails(tv_id = id) }

    suspend fun getSimilarTvSeries(id: Int) = safeApiCall { tvSeriesApi.getSimilarTVSeries(tv_id = id) }

    suspend fun getTvSeriesCast(id: Int) = safeApiCall { peopleApi.getTVCast(tv_id = id) }

    suspend fun insertTvSeries(tvSeriesEntry: TVSeriesEntry) = database.getTvSeriesDao().insertTvSerie(tvSeriesEntry)

    suspend fun deleteTvSeries(tvSeriesEntry: TVSeriesEntry) = database.getTvSeriesDao().deleteTvSerie(tvSeriesEntry)

    fun isTvSeriesExist(id: Int) = database.getTvSeriesDao().isTvSeriesExist(id)
}