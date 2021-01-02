package com.mertkesgin.discovermovieapp.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel(
    private val repository: BaseRepository
) : ViewModel() {

}