package com.example.litelentanews.data.api

import com.example.litelentanews.util.Result
import okhttp3.Response

interface DataSource {
    fun loadRssFile(beginDate:Long): Result<List<News_DTO>>
    fun handleRssFile(response:Response, beginDate:Long)
    fun loadHtmlPageNews(url:String):String
    fun getConstName():String
    fun parseDate(date:String):Long
}