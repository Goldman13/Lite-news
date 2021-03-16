package com.example.litelentanews.di

import com.example.litelentanews.domain.GetNewsListCategoryUseCase
import com.example.litelentanews.domain.GetNewsListCategoryUseCaseImpl
import com.example.litelentanews.domain.model.NewsItemModel
import com.example.litelentanews.domain.usecase.*
import com.example.litelentanews.util.Result
import dagger.Binds
import dagger.Module

@Module
abstract class UseCaseModule {
    @Binds
    abstract fun update(useCase:UpdateNewsUseCaseImpl):UpdateNewsUseCase<Result<*>>
    @Binds
    abstract fun getAllNews(useCase:GetAllCompanyNewsUseCaseImpl):GetAllCompanyNewsUseCase<NewsItemModel>
    @Binds
    abstract fun getNewsDescript(useCase:GetNewsDescriptionUseCaseImpl):GetNewsDescriptionUseCase
    @Binds
    abstract fun getNewsCategory(useCase:GetNewsListCategoryUseCaseImpl): GetNewsListCategoryUseCase<NewsItemModel>
    @Binds
    abstract fun getAllCategory(useCase:GetAllCategoryUseCaseImpl): GetAllCategoryUseCase
    @Binds
    abstract fun delete(useCase: DeleteAllOldNewsUseCaseImpl):DeleteAllOldNewsUseCase
    @Binds
    abstract fun changeFavorite(useCase: ChangeFavoriteStatusNewsUseCaseImpl):ChangeFavoriteStatusNewsUseCase
}