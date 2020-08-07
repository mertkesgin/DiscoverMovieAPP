package com.mertkesgin.discovermovieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.model.entry.TVSeriesEntry
import com.mertkesgin.discovermovieapp.utils.Constants.POSTER_BASE_URL
import com.mertkesgin.discovermovieapp.utils.PicassoImageHelper

class SliderTVAdapter(
    private val trendsList: List<TVSeriesEntry>
): RecyclerView.Adapter<SliderTVAdapter.SliderTVViewHolder>() {

    private val picassoImageHelper = PicassoImageHelper()

    inner class SliderTVViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        val tvTVSeriesTitle : TextView = itemView.findViewById(R.id.tv_slider_name)
        val imgTVSeries : ImageView = itemView.findViewById(R.id.img_movie_slider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderTVViewHolder {
        return SliderTVViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_slider,parent,false))
    }

    override fun getItemCount(): Int {
        return trendsList.size
    }

    override fun onBindViewHolder(holder: SliderTVViewHolder, position: Int) {
        picassoImageHelper.loadUrl(POSTER_BASE_URL+trendsList[position].backdrop_path,holder.imgTVSeries)
        holder.tvTVSeriesTitle.text = trendsList[position].name
    }
}