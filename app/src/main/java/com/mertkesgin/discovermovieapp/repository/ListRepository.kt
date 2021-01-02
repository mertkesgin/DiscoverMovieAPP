package com.mertkesgin.discovermovieapp.repository

import com.mertkesgin.discovermovieapp.base.BaseRepository
import com.mertkesgin.discovermovieapp.data.local.AppDatabase

class ListRepository(
    private val database: AppDatabase
) : BaseRepository() {

    val getMovieList = database.getMovieDao().getMovieList()

    val getTvSeriesList = database.getTvSeriesDao().getTvList()
}