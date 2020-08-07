package com.mertkesgin.discovermovieapp.utils

import android.widget.ImageView
import com.squareup.picasso.Picasso

class PicassoImageHelper : IImageProcess {

    private val mPicassoInstance = Picasso.get()

    override fun loadUrl(url: String, imageView: ImageView) {
        mPicassoInstance.load(url).into(imageView)
    }

    override fun loadUrlWithPlaceHolder(url: String, imageView: ImageView, placeHolderRes: Int) {
        mPicassoInstance.load(url).placeholder(placeHolderRes).into(imageView)
    }
}