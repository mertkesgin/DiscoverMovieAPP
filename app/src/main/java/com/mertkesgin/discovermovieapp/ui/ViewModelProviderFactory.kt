package com.mertkesgin.discovermovieapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mertkesgin.discovermovieapp.repository.AppRepository
import com.mertkesgin.discovermovieapp.ui.list.ListViewModel
import com.mertkesgin.discovermovieapp.ui.movie.MovieViewModel
import com.mertkesgin.discovermovieapp.ui.moviedetails.MovieDetailsViewModel
import com.mertkesgin.discovermovieapp.ui.search.SearchViewModel
import com.mertkesgin.discovermovieapp.ui.tvdetails.TVDetailsViewModel
import com.mertkesgin.discovermovieapp.ui.tvseries.TVSeriesViewModel
import java.lang.IllegalArgumentException

class ViewModelProviderFactory (
    private val appRepository: AppRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MovieViewModel::class.java) -> {
                return MovieViewModel(appRepository) as T
            }
            modelClass.isAssignableFrom(TVSeriesViewModel::class.java) -> {
                return TVSeriesViewModel(appRepository) as T
            }
            modelClass.isAssignableFrom(MovieDetailsViewModel::class.java) -> {
                return MovieDetailsViewModel(appRepository) as T
            }
            modelClass.isAssignableFrom(TVDetailsViewModel::class.java) -> {
                return TVDetailsViewModel(appRepository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                return SearchViewModel(appRepository) as T
            }
            modelClass.isAssignableFrom(ListViewModel::class.java) -> {
                return ListViewModel(appRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
