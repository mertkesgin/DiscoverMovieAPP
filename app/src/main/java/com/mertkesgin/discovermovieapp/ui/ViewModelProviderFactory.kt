package com.mertkesgin.discovermovieapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mertkesgin.discovermovieapp.repository.MovieRepository
import com.mertkesgin.discovermovieapp.ui.movie.MovieViewModel
import com.mertkesgin.discovermovieapp.ui.moviedetails.MovieDetailsViewModel
import com.mertkesgin.discovermovieapp.ui.search.SearchViewModel
import com.mertkesgin.discovermovieapp.ui.tvdetails.TVDetailsViewModel
import com.mertkesgin.discovermovieapp.ui.tvseries.TVSeriesViewModel
import java.lang.IllegalArgumentException

class ViewModelProviderFactory (
    private val movieRepository: MovieRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)){
            return MovieViewModel(movieRepository) as T
        }else if (modelClass.isAssignableFrom(TVSeriesViewModel::class.java)){
            return TVSeriesViewModel(movieRepository) as T
        }else if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)){
            return MovieDetailsViewModel(movieRepository) as T
        }else if (modelClass.isAssignableFrom(TVDetailsViewModel::class.java)){
            return TVDetailsViewModel(movieRepository) as T
        }else if (modelClass.isAssignableFrom(SearchViewModel::class.java)){
            return SearchViewModel(movieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
