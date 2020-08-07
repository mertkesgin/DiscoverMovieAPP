package com.mertkesgin.discovermovieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.model.entry.PeopleEntry
import com.mertkesgin.discovermovieapp.utils.Constants.POSTER_BASE_URL
import com.mertkesgin.discovermovieapp.utils.PicassoImageHelper
import kotlinx.android.synthetic.main.item_people.view.*

class PeopleAdapter: RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>() {

    private val picassoImageHelper = PicassoImageHelper()

    inner class PeopleViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<PeopleEntry>(){
        override fun areItemsTheSame(oldItem: PeopleEntry, newItem: PeopleEntry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PeopleEntry, newItem: PeopleEntry): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        return PeopleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_people,
                parent,
                false
            ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        val people = differ.currentList[position]
        holder.itemView.apply {
            picassoImageHelper.loadUrl(POSTER_BASE_URL+people.profile_path,img_people)
            tv_people_name.text = people.name
        }
    }
}