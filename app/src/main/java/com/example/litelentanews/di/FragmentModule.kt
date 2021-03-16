package com.example.litelentanews.di

import com.example.litelentanews.ui.categorynews.ListNewsCategoriesFragment
import com.example.litelentanews.ui.listnews.ListNewsFragment
import com.example.litelentanews.ui.start.StartFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentModule {
    @ContributesAndroidInjector
    fun provide_listNewsFragment():ListNewsFragment

    @ContributesAndroidInjector
    fun provide_listNewsCategoriesFragment(): ListNewsCategoriesFragment

    @ContributesAndroidInjector
    fun provide_StartFragment(): StartFragment
}