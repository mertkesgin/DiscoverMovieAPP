package com.mertkesgin.discovermovieapp.repository

import com.mertkesgin.discovermovieapp.base.BaseRepository
import com.mertkesgin.discovermovieapp.data.local.AppDatabase
import com.mertkesgin.discovermovieapp.data.remote.TvSeriesApi

class TvSeriesRepository(
    private val tvSeriesApi: TvSeriesApi
) : BaseRepository() {

    suspend fun getTrendsOfTv() = safeApiCall { tvSeriesApi.getTrendsOfDayTV() }

    suspend fun getPopularTvSeries() = safeApiCall { tvSeriesApi.getPopularTVSeries() }

    suspend fun getTopRatedTVSeries() = safeApiCall { tvSeriesApi.getTopRatedTV() }
}