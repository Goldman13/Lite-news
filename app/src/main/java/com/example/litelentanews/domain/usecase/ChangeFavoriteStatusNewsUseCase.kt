package com.example.litelentanews.domain.usecase

import com.example.litelentanews.domain.model.NewsItemModel
import com.example.litelentanews.domain.repository.NewsRepository
import javax.inject.Inject

interface ChangeFavoriteStatusNewsUseCase {
    suspend operator fun invoke(guid:String, value:Boolean)
}

class ChangeFavoriteStatusNewsUseCaseImpl @Inject constructor(
        private val repository: NewsRepository<NewsItemModel>
):ChangeFavoriteStatusNewsUseCase{
    override suspend fun invoke(guid: String, value:Boolean) {
        repository.changeFavorite(guid, value)
    }
}