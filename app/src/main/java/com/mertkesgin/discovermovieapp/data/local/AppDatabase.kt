package com.mertkesgin.discovermovieapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mertkesgin.discovermovieapp.model.entry.MovieEntry
import com.mertkesgin.discovermovieapp.model.entry.TVSeriesEntry

@Database(
    entities = [MovieEntry::class,TVSeriesEntry::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getMovieDao(): MovieDao

    abstract fun getTvSeriesDao(): TvSeriesDao

    companion object{
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "appdata.db"
            ).build()
    }
}