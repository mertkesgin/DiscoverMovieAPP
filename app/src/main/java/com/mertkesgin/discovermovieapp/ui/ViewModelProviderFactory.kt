package com.mertkesgin.discovermovieapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mertkesgin.discovermovieapp.base.BaseRepository
import com.mertkesgin.discovermovieapp.repository.*
import com.mertkesgin.discovermovieapp.ui.list.ListViewModel
import com.mertkesgin.discovermovieapp.ui.movie.MovieViewModel
import com.mertkesgin.discovermovieapp.ui.moviedetails.MovieDetailsViewModel
import com.mertkesgin.discovermovieapp.ui.search.SearchViewModel
import com.mertkesgin.discovermovieapp.ui.tvdetails.TVDetailsViewModel
import com.mertkesgin.discovermovieapp.ui.tvseries.TVSeriesViewModel
import java.lang.IllegalArgumentException

class ViewModelProviderFactory (
    private val repository: BaseRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MovieViewModel::class.java) -> MovieViewModel(repository as MovieRepository) as T
            modelClass.isAssignableFrom(TVSeriesViewModel::class.java) -> TVSeriesViewModel(repository as TvSeriesRepository) as T
            modelClass.isAssignableFrom(MovieDetailsViewModel::class.java) -> MovieDetailsViewModel(repository as MovieDetailsRepository) as T
            modelClass.isAssignableFrom(TVDetailsViewModel::class.java) -> TVDetailsViewModel(repository as TvSeriesDetailsRepository) as T
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(repository as SearchRepository) as T
            modelClass.isAssignableFrom(ListViewModel::class.java) -> ListViewModel(repository as ListRepository) as T
            else -> throw IllegalArgumentException("ViewModelClass Not Found")
        }
    }
}
