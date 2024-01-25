package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVerify.setOnClickListener {
            binding.btnVerify.visibility = View.GONE
            binding.linearLayoutInputFil.visibility = View.VISIBLE
            binding.btnSignIn.visibility = View.VISIBLE


        }
        binding.emailInputLayout.setEndIconOnClickListener {
            Toast.makeText(this, "end icon", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onResume() {
        super.onResume()
        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this, SelectYourCityActivity::class.java)
            startActivity(intent)
        }
    }
}