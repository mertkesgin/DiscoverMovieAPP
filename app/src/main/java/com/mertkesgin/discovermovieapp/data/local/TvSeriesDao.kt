package com.mertkesgin.discovermovieapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mertkesgin.discovermovieapp.model.entry.TVSeriesEntry

@Dao
interface TvSeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvSerie(tvSeriesEntry: TVSeriesEntry)

    @Delete
    suspend fun deleteTvSerie(tvSeriesEntry: TVSeriesEntry)

    @Query("SELECT * FROM tv_table")
    fun getTvList(): LiveData<List<TVSeriesEntry>>

    @Query("SELECT EXISTS(SELECT * FROM tv_table WHERE tvSeriesId = :tvSeriesId)")
    fun isTvSeriesExist(tvSeriesId:Int): LiveData<Boolean>
}