package com.example.newsapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.ItemCityBinding

class SearchCityAdapter(private val context: Context,private var list: List<SearchCity>)
    :RecyclerView.Adapter<SearchCityAdapter.ViewHolder>() {

        inner  class ViewHolder(binding: ItemCityBinding):RecyclerView.ViewHolder(binding.root){

            val city=binding.cityName
            val icon=binding.icon
            val linearlayout=binding.linearLayout

        }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {

        return ViewHolder(
            ItemCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {

        val model=list[position]
        holder.city.text=model.city
        holder.icon.setImageResource(R.drawable.cursor)
        if (position==0){
            holder.linearlayout.setBackgroundColor(context.resources.getColor(R.color.blue))
        }


    }
    fun setFilteredList(list: ArrayList<SearchCity>){
        this.list=list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
       return list.size
    }


}