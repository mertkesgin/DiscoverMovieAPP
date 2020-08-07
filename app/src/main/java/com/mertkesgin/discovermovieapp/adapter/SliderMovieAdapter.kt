package com.mertkesgin.discovermovieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.model.entry.MovieEntry
import com.mertkesgin.discovermovieapp.utils.Constants.POSTER_BASE_URL
import com.mertkesgin.discovermovieapp.utils.PicassoImageHelper

class SliderMovieAdapter(
    private val trendsList: List<MovieEntry>
): RecyclerView.Adapter<SliderMovieAdapter.SliderViewHolder>() {

    private val picassoImageHelper = PicassoImageHelper()

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvMovieTitle : TextView = itemView.findViewById(R.id.tv_slider_name)
        val imgMovie : ImageView = itemView.findViewById(R.id.img_movie_slider)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SliderMovieAdapter.SliderViewHolder {
        return SliderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_slider,parent,false))
    }

    override fun getItemCount(): Int {
        return trendsList.size
    }

    override fun onBindViewHolder(holder: SliderMovieAdapter.SliderViewHolder, position: Int) {
        holder.tvMovieTitle.text = trendsList[position].original_title
        picassoImageHelper.loadUrl(POSTER_BASE_URL+trendsList[position].backdrop_path,holder.imgMovie)
    }
}