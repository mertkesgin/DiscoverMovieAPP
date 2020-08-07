package com.mertkesgin.discovermovieapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mertkesgin.discovermovieapp.R
import com.mertkesgin.discovermovieapp.model.ProductionCompany
import com.mertkesgin.discovermovieapp.utils.Constants.COMPANY_LOGO_URL
import com.mertkesgin.discovermovieapp.utils.Constants.POSTER_BASE_URL
import com.mertkesgin.discovermovieapp.utils.PicassoImageHelper
import kotlinx.android.synthetic.main.item_companies.view.*

class CompanyAdapter : RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {

    private val picassoImageHelper = PicassoImageHelper()

    inner class CompanyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)

    val differCallback = object : DiffUtil.ItemCallback<ProductionCompany>(){
        override fun areItemsTheSame(oldItem: ProductionCompany,newItem: ProductionCompany): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductionCompany,newItem: ProductionCompany): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        return CompanyViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_companies,
            parent,
            false
        ))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        val company = differ.currentList[position]
        holder.itemView.apply {
            tv_company_name.text = company.name
        }
    }


}