package com.mertkesgin.discovermovieapp.ui.list

import com.mertkesgin.discovermovieapp.repository.AppRepository
import com.mertkesgin.discovermovieapp.ui.BaseViewModel

class ListViewModel (
    val appRepository: AppRepository
): BaseViewModel() {

    val movieList = appRepository.getMovieList()

    val tvList = appRepository.getTvList()
}