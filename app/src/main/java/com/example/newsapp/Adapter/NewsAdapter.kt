package com.example.newsapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.databinding.NewsItemBinding
import com.example.newsapp.model.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(private val binding: NewsItemBinding)
        :RecyclerView.ViewHolder(binding.root){
        val ivArticleImage=binding.ivArticleImage
        val author=binding.publisherName
        val tvTitle=binding.newsHeading
        val tvDescription=binding.textView
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

        return ArticleViewHolder(NewsItemBinding.inflate
            (LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

        val article=differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this)
                .load(article.urlToImage)
                .placeholder(R.drawable.landscape_placeholder_svgrepo_com)

                .into(holder.ivArticleImage)
            holder.author.text=article.author
            holder.tvTitle.text=article.title
            holder.tvDescription.text=article.description
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