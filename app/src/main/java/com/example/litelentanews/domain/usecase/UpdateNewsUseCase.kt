package com.example.litelentanews.domain.usecase

import com.example.litelentanews.data.api.DataSource
import com.example.litelentanews.domain.model.NewsItemModel
import com.example.litelentanews.domain.repository.NewsRepository
import com.example.litelentanews.util.Result
import javax.inject.Inject

interface UpdateNewsUseCase<T> {
    operator suspend fun invoke(dataClass:Class<out DataSource>):T
}

class UpdateNewsUseCaseImpl @Inject constructor(
        private val repository:NewsRepository<NewsItemModel>
):UpdateNewsUseCase<Result<*>> {
    override suspend fun invoke(dataClass:Class<out DataSource>): Result<*> {
        repository.dataSourseClass = dataClass
        return repository.loadNews()
    }
}