package com.example.newsapp.Adapter

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newsapp.R
import com.example.newsapp.model.ImageItem

class CarousalViewPagerAdapter : ListAdapter<ImageItem,CarousalViewPagerAdapter.ViewHolder>(DiffCallback()){


    class DiffCallback  : DiffUtil.ItemCallback<ImageItem>(){
        override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
            return oldItem == newItem
        }

    }

    class ViewHolder(iteView: View): RecyclerView.ViewHolder(iteView){
        private val imageView = iteView.findViewById<ImageView>(R.id.imageview)
        private val newsHeadline=iteView.findViewById<TextView>(R.id.news_headline)


        fun bindData(item: ImageItem){
            Glide.with(itemView)
                .load(item.url)
                .placeholder(R.drawable.landscape_placeholder_svgrepo_com)

                .into(imageView)
            newsHeadline.text=item.newsHeadline
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.carousel_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageItem = getItem(position)
        holder.bindData(imageItem)

        /*if (position==currentList.size-2){
            handler.post(runnable)

        }*/
    }
    /*private val handler= Handler()
    private val runnable = Runnable {
        submitList(currentList.toMutableList().apply { addAll(currentList) })
    }*/

}