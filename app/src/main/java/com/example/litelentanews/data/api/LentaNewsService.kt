package com.example.litelentanews.data.api

import androidx.lifecycle.LiveData
import com.example.litelentanews.util.Result
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.GET

interface LentaNewsService {
    @GET("/rss/news")
    suspend fun load():ResponseBody
}