package com.example.litelentanews.domain.repository

import androidx.lifecycle.LiveData
import com.example.litelentanews.data.api.DataSource
import com.example.litelentanews.domain.model.Category
import com.example.litelentanews.domain.model.NewsItemModel
import com.example.litelentanews.util.Result

abstract class NewsRepository<T> {
    abstract var dataSourseClass:Class<out DataSource>
    abstract val dataSourceInst:DataSource
    abstract suspend fun loadNews():Result<*>
    abstract fun loadDescriptionNews(url:String):String
    abstract fun getNewsListCategory_DB(category:String):LiveData<List<T>>
    abstract fun getAllCategory_DB():LiveData<List<Category>>
    abstract fun getAllCompanyNews():LiveData<List<NewsItemModel>>
    abstract suspend fun deleteOldNews():Result<*>
    abstract suspend fun changeFavorite(guid: String, value:Boolean)
}