package com.mertkesgin.discovermovieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.model.entry.TVSeriesEntry
import com.mertkesgin.discovermovieapp.utils.Constants
import com.mertkesgin.discovermovieapp.utils.PicassoImageHelper
import kotlinx.android.synthetic.main.item_movie.view.*

class TVAdapter() : RecyclerView.Adapter<TVAdapter.PopularTVViewHolder> (){

    private val picassoImageHelper = PicassoImageHelper()

    inner class PopularTVViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<TVSeriesEntry>(){

        override fun areItemsTheSame(oldItem: TVSeriesEntry, newItem: TVSeriesEntry): Boolean {
            return oldItem.tvSeriesId == newItem.tvSeriesId
        }

        override fun areContentsTheSame(oldItem: TVSeriesEntry, newItem: TVSeriesEntry): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularTVViewHolder {
        return PopularTVViewHolder(
            LayoutInflater.from(parent.context).inflate(
            R.layout.item_movie,
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((TVSeriesEntry) -> Unit)? = null

    override fun onBindViewHolder(holder: PopularTVViewHolder, position: Int) {
        val tvSeries = differ.currentList[position]
        holder.itemView.apply {
            picassoImageHelper.loadUrl(Constants.POSTER_BASE_URL +tvSeries.poster_path,img_movie)
            tv_movie_name.text = tvSeries.original_name
            tv_movie_vote_average.text = tvSeries.vote_average.toString()
            ratingBar.rating = ((tvSeries.vote_average)/2).toFloat()

            setOnClickListener {
                onItemClickListener?.let { it(tvSeries) }
            }
        }
    }

    fun setOnItemClickListener(listener: (TVSeriesEntry) -> Unit){
        onItemClickListener = listener
    }
}