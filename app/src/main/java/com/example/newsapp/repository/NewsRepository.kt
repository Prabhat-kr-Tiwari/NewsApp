package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitInstance

class NewsRepository {

    suspend fun getBreakingNews(countryCode: String,pageNumber:Int)=
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun getNewsBasedOnCategory(countryCode:String,pageNumber: Int,category:String)=
        RetrofitInstance.api.getNewsBasedOnCategory(countryCode,pageNumber,category)
}