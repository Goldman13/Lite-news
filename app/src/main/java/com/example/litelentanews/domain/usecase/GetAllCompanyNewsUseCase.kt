package com.example.litelentanews.domain.usecase

import androidx.lifecycle.LiveData
import com.example.litelentanews.data.api.DataSource
import com.example.litelentanews.domain.model.NewsItemModel
import com.example.litelentanews.domain.repository.NewsRepository
import javax.inject.Inject

interface GetAllCompanyNewsUseCase<T> {
    operator fun invoke(dataClass:Class<out DataSource>):LiveData<List<T>>
}

class GetAllCompanyNewsUseCaseImpl @Inject constructor(
        private val repository:NewsRepository<*>
):GetAllCompanyNewsUseCase<NewsItemModel> {
    override fun invoke(dataClass:Class<out DataSource>): LiveData<List<NewsItemModel>> {
        repository.dataSourseClass = dataClass
        return repository.getAllCompanyNews()
    }
}