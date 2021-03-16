package com.example.litelentanews.domain.usecase

import com.example.litelentanews.data.api.DataSource
import com.example.litelentanews.domain.model.NewsItemModel
import com.example.litelentanews.domain.repository.NewsRepository
import javax.inject.Inject

interface GetNewsDescriptionUseCase{
    operator fun invoke(url:String, dataClass:Class<out DataSource>):String
}

class GetNewsDescriptionUseCaseImpl @Inject constructor(
        val repository: NewsRepository<NewsItemModel>
):GetNewsDescriptionUseCase {
    override fun invoke(url: String, dataClass:Class<out DataSource>): String {
        repository.dataSourseClass = dataClass
        return repository.loadDescriptionNews(url)
    }
}