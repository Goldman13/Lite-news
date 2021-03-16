package com.example.litelentanews.domain.usecase

import com.example.litelentanews.domain.model.NewsItemModel
import com.example.litelentanews.domain.repository.NewsRepository
import com.example.litelentanews.util.Result
import javax.inject.Inject

interface DeleteAllOldNewsUseCase{
    suspend operator fun invoke():Result<*>
}

class DeleteAllOldNewsUseCaseImpl @Inject constructor(
    val repository: NewsRepository<NewsItemModel>
) :DeleteAllOldNewsUseCase {
    override suspend fun invoke(): Result<*> {
        return repository.deleteOldNews()
    }
}