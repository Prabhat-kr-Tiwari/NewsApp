package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.ActivitySelectYourCityBinding
import java.util.Locale

class SelectYourCityActivity : AppCompatActivity() {
    private lateinit var list: ArrayList<SearchCity>
    lateinit var binding: ActivitySelectYourCityBinding

    //    private lateinit var adapter: SearchCityAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectYourCityBinding.inflate(layoutInflater)

        setContentView(binding.root)
        fillCity()

//        val search_edit=findViewById<TextInputEditText>(R.id.search_edit)
        /* val editText=findViewById<AutoCompleteTextView>(R.id.autoComplete_txt)
         val adapter=AutoCompleteSearchCityAdapter(this,list)
        editText.setAdapter(adapter)
         Log.d("PRABHAT", "onCreate: here")*/
        binding.recylerview.layoutManager = LinearLayoutManager(this)
        binding.recylerview.setHasFixedSize(true)
        binding.recylerview.adapter = SearchCityAdapter(this, list)


        binding.searchView.requestFocus()
        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
        binding.btnSelectCity.setOnClickListener {
            val intent= Intent(this,HomeActivity::class.java)
            startActivity(intent)
        }


    }

    private fun filterList(newText: String?) {
        if (newText != null) {
            val filteredList = ArrayList<SearchCity>()
            for (i in list) {
                if (i.city!!.lowercase(Locale.ROOT).contains(newText)) {
                    filteredList.add(i)

                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No Dev Found", Toast.LENGTH_SHORT).show()
            } else {


                binding.recylerview.adapter = SearchCityAdapter(this, filteredList)


            }

        }

    }

    private fun fillCity() {
        list = arrayListOf()

        list.add(SearchCity(R.drawable.cursor, "Delhi"))
        list.add(SearchCity(R.drawable.cursor, "Agra"))
        list.add(SearchCity(R.drawable.cursor, "Noida"))
        list.add(SearchCity(R.drawable.cursor, "Mumbai"))
        list.add(SearchCity(R.drawable.cursor, "Agra"))
        list.add(SearchCity(R.drawable.cursor, "Noida"))
        list.add(SearchCity(R.drawable.cursor, "Bangalore"))
        list.add(SearchCity(R.drawable.cursor, "Delhi"))
        list.add(SearchCity(R.drawable.cursor, "Agra"))
        list.add(SearchCity(R.drawable.cursor, "Noida"))
        list.add(SearchCity(R.drawable.cursor, "Mumbai"))
        list.add(SearchCity(R.drawable.cursor, "Agra"))
        list.add(SearchCity(R.drawable.cursor, "Noida"))
        list.add(SearchCity(R.drawable.cursor, "Bangalore"))
        list.add(SearchCity(R.drawable.cursor, "Noida"))
        list.add(SearchCity(R.drawable.cursor, "Mumbai"))
        list.add(SearchCity(R.drawable.cursor, "Agra"))
        list.add(SearchCity(R.drawable.cursor, "Noida"))
        list.add(SearchCity(R.drawable.cursor, "Bangalore"))

    }
}
