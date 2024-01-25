package com.example.newsapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.LatestNewsItemBinding
import com.example.newsapp.databinding.RecomendationTopicItemBinding
import com.example.newsapp.model.Article


class AdsFragmentAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    //diffutil
    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {

            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {

            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    inner class LatestNewsViewHolder(private val binding: LatestNewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivArticleImage = binding.articleImage
        val tvArticle = binding.txtHeadline
        val source = binding.txtSource
        val time = binding.txtTime

    }
    inner class RecomendationViewHolder
        (private val binding:RecomendationTopicItemBinding):RecyclerView.ViewHolder(binding.root){
        val ivArticleImage = binding.image
        val tvArticle = binding.txtNewsHeadline
        val source = binding.txtSource
        val time = binding.txtTime

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TYPE_ARTICLE -> LatestNewsViewHolder(
                LatestNewsItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            TYPE_RECOMMENDATION_ITEM -> RecomendationViewHolder(
                RecomendationTopicItemBinding.inflate
                    (LayoutInflater.from(parent.context),parent,false))
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder){
            is AdsFragmentAdapter<*>.LatestNewsViewHolder ->{
                val article=differ.currentList[position]
                holder.itemView.apply {
                    Glide.with(this).load(article.urlToImage)
                        .placeholder(R.drawable.landscape_placeholder_svgrepo_com)
                        .into(holder.ivArticleImage)

                    holder.source.text=article.source?.name
                    holder.tvArticle.text=article.title
                    holder.time.text=article.publishedAt
                }

            }

            is AdsFragmentAdapter<*>.RecomendationViewHolder->{
                val article=differ.currentList[position]
                holder.itemView.apply {
                    Glide.with(this).load(article.urlToImage)
                        .placeholder(R.drawable.landscape_placeholder_svgrepo_com)
                        .into(holder.ivArticleImage)

                    holder.source.text=article.source?.name
                    holder.tvArticle.text=article.title
                    holder.time.text=article.publishedAt
                }

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is Article -> TYPE_ARTICLE
            /*is Test -> TYPE_RECOMMENDATION_ITEM*/
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }
    private var onItemClickListener: ((T) -> Unit)? = null

    fun setOnItemClickListener(listener: (T) -> Unit) {
        onItemClickListener = listener
    }
    companion object {
        private const val TYPE_ARTICLE = 1
        private const val TYPE_RECOMMENDATION_ITEM = 2
    }
}