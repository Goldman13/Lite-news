package com.example.litelentanews.domain

import androidx.lifecycle.LiveData
import com.example.litelentanews.data.api.DataSource
import com.example.litelentanews.domain.model.NewsItemModel
import com.example.litelentanews.domain.repository.NewsRepository
import javax.inject.Inject

interface GetNewsListCategoryUseCase<T>{
    operator fun invoke(dataClass:Class<out DataSource>, categoryName:String):LiveData<List<T>>
}

class GetNewsListCategoryUseCaseImpl @Inject constructor(
    val repository:NewsRepository<NewsItemModel>
    ):GetNewsListCategoryUseCase<NewsItemModel>
{
    override fun invoke(dataClass:Class<out DataSource>,categoryName:String): LiveData<List<NewsItemModel>> {
        repository.dataSourseClass = dataClass
        return repository.getNewsListCategory_DB(categoryName)
    }
}