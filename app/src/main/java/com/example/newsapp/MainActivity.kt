package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
   lateinit var  topAnimation:Animation
   lateinit var  bottomAnimation:Animation
   lateinit var imageView: ImageView
   lateinit var txt:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        imageView=findViewById<ImageView>(R.id.image)
        txt=findViewById(R.id.txt_all_news)
        topAnimation=AnimationUtils.loadAnimation(this,R.anim.top_anim)
        bottomAnimation=AnimationUtils.loadAnimation(this,R.anim.bottom_anim)
        imageView.animation=topAnimation
        txt.animation=bottomAnimation
        Handler(Looper.getMainLooper()).postDelayed(Runnable {

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()

        }, 3000)
    }
}