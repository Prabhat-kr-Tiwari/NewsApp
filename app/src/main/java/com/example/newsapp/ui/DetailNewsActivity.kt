package com.example.newsapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.newsapp.Adapter.CarousalViewPagerAdapter
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityDetailNewsBinding
import com.example.newsapp.model.Article
import com.example.newsapp.model.ImageItem
import java.util.UUID

class DetailNewsActivity : AppCompatActivity() {
    lateinit var binding :ActivityDetailNewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extras = intent.extras
        val article = intent.getSerializableExtra("key") as? Article
        binding.authorName.text=article?.author
        Glide.with(this)
            .load(article?.urlToImage).placeholder(R.drawable.landscape_placeholder_svgrepo_com)
            .into(binding.image)
        binding.newsHeadline.text=article?.title
        binding.newsDescription.text=article?.description
        binding.authorName.text=article?.author
        val urlOfNews=article?.url
        binding.shareNews.setOnClickListener {
            val intent=Intent()
            intent.setAction(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT,urlOfNews)
            intent.setType("text/plain")
            if (intent.resolveActivity(packageManager)!=null){
                startActivity(intent)
            }

        }
        binding.backBtn.setOnClickListener {
            finish()
        }
        binding.shareButton.setOnClickListener {
            val intent=Intent()
            intent.setAction(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT,urlOfNews)
            intent.setType("text/plain")
            if (intent.resolveActivity(packageManager)!=null){
                startActivity(intent)
            }
        }




    }


}