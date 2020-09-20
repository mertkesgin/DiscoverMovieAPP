package com.mertkesgin.discovermovieapp.utils

import android.view.View
import android.widget.ProgressBar

object Constants {

    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500"
    const val COMPANY_LOGO_URL = "https://image.tmdb.org/t/p/w185"
    const val API_KEY = "9fe7ed6c55f0b13014ea81d29e0f1333"
    const val LANGUAGE_EN = "en-US"
    const val LANGUAGE_TR = "tr"
    const val SEARCH_TIME_DELAY = 1000L

    fun hideProgress(progressBar: ProgressBar){
        progressBar.visibility = View.GONE
    }

    fun showProgress(progressBar: ProgressBar){
        progressBar.visibility = View.VISIBLE
    }
}