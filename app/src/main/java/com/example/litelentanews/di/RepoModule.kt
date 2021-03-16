package com.example.litelentanews.di

import com.example.litelentanews.data.repo.NewsRepositoryImpl
import com.example.litelentanews.domain.model.NewsItemModel
import com.example.litelentanews.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepoModule {
    @Binds
    abstract fun getRepo(repo:NewsRepositoryImpl):NewsRepository<NewsItemModel>
    @Binds
    abstract fun getRepoAny(repo:NewsRepositoryImpl):NewsRepository<*>
}