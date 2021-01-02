package com.mertkesgin.discovermovieapp.ui.list

import com.mertkesgin.discovermovieapp.base.BaseViewModel
import com.mertkesgin.discovermovieapp.repository.ListRepository

class ListViewModel (
    private val repository: ListRepository
) : BaseViewModel(repository){

    val movieList = repository.getMovieList

    val tvSeriesList = repository.getTvSeriesList
}