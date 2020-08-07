package com.mertkesgin.discovermovieapp.utils

import android.widget.ImageView

interface IImageProcess {

    fun loadUrl(url:String,imageView:ImageView)

    fun loadUrlWithPlaceHolder(url:String,imageView:ImageView,placeHolderRes:Int)
}