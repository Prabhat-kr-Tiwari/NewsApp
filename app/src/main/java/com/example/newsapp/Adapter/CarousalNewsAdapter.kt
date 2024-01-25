package com.example.newsapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.CarouselItemBinding
import com.example.newsapp.databinding.NewsItemBinding
import com.example.newsapp.model.Article

class CarousalNewsAdapter : RecyclerView.Adapter<CarousalNewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(private val binding: CarouselItemBinding):RecyclerView.ViewHolder(binding.root){
        val ivArticleImage=binding.imageview

        val tvTitle=binding.newsHeadline

    }
    //diffutil
    private val differCallback=object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {

            return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {

            return oldItem==newItem
        }

    }
    val differ=AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {

        return ArticleViewHolder(CarouselItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

        val article=differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(holder.ivArticleImage)
            holder.tvTitle.text=article.title
            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    private var onItemClickListener: ((Article) -> Unit)? = null
    fun setOnItemClickListener(listner: (Article) -> Unit) {
        onItemClickListener = listner
    }
}