package com.mertkesgin.discovermovieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.model.entry.MovieEntry
import com.mertkesgin.discovermovieapp.utils.Constants.POSTER_BASE_URL
import com.mertkesgin.discovermovieapp.utils.PicassoImageHelper
import kotlinx.android.synthetic.main.item_movie.view.*

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.PopularMoviesViewHolder>() {

    private val picassoImageHelper = PicassoImageHelper()

    inner class PopularMoviesViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<MovieEntry>(){
        override fun areItemsTheSame(oldItem: MovieEntry, newItem: MovieEntry): Boolean {
            return oldItem.movieId == newItem.movieId
        }

        override fun areContentsTheSame(oldItem: MovieEntry, newItem: MovieEntry): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesViewHolder {
        return PopularMoviesViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_movie,
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((MovieEntry) -> Unit)? = null

    override fun onBindViewHolder(holder: PopularMoviesViewHolder, position: Int) {
        val movie = differ.currentList[position]
        holder.itemView.apply {
            picassoImageHelper.loadUrl(POSTER_BASE_URL+movie.poster_path,img_movie)
            tv_movie_name.text = movie.original_title
            tv_movie_vote_average.text = movie.vote_average.toString()
            ratingBar.rating = ((movie.vote_average)/2).toFloat()

            setOnClickListener {
                onItemClickListener?.let { it(movie) }
            }
        }
    }

    fun setOnItemClickListener(listener: (MovieEntry) -> Unit){
        onItemClickListener = listener
    }
}