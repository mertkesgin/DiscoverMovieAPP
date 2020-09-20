package com.mertkesgin.discovermovieapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mertkesgin.discovermovieapp.model.entry.MovieEntry

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movieEntry: MovieEntry)

    @Delete
    suspend fun deleteMovie(movieEntry: MovieEntry)

    @Query("SELECT * FROM movie_table")
    fun getMovieList(): LiveData<List<MovieEntry>>

    @Query("SELECT EXISTS(SELECT * FROM movie_table WHERE movieId = :movieId)")
    fun isMovieExist(movieId:Int):LiveData<Boolean>
}