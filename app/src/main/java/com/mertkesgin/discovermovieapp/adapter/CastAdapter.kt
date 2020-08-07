package com.mertkesgin.discovermovieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.model.entry.CastEntry
import com.mertkesgin.discovermovieapp.utils.Constants.POSTER_BASE_URL
import com.mertkesgin.discovermovieapp.utils.PicassoImageHelper
import kotlinx.android.synthetic.main.item_cast.view.*

class CastAdapter: RecyclerView.Adapter<CastAdapter.CastViewHolder>() {

    private val picassoImageHelper = PicassoImageHelper()

    inner class CastViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)

    val differCallback = object : DiffUtil.ItemCallback<CastEntry>(){
        override fun areItemsTheSame(oldItem: CastEntry, newItem: CastEntry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CastEntry, newItem: CastEntry): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_cast,
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val cast = differ.currentList[position]
        holder.itemView.apply {
            tv_cast_name.text = cast.name
            tv_cast_character.text = cast.character
            picassoImageHelper.loadUrl(POSTER_BASE_URL+cast.profile_path,img_cast)
        }
    }
}