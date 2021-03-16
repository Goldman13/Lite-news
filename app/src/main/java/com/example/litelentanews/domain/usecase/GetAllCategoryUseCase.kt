package com.example.litelentanews.domain.usecase

import androidx.lifecycle.LiveData
import com.example.litelentanews.data.api.DataSource
import com.example.litelentanews.domain.model.Category
import com.example.litelentanews.domain.model.NewsItemModel
import com.example.litelentanews.domain.repository.NewsRepository
import javax.inject.Inject

interface GetAllCategoryUseCase {
    operator fun invoke(dataClass:Class<out DataSource>):LiveData<List<Category>>
}

class GetAllCategoryUseCaseImpl @Inject constructor(
    private val repository:NewsRepository<NewsItemModel>
):GetAllCategoryUseCase {
    override fun invoke(dataClass:Class<out DataSource>): LiveData<List<Category>> {
        repository.dataSourseClass = dataClass
        return repository.getAllCategory_DB()
    }
}