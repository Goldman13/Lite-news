package com.example.litelentanews.di

import com.example.litelentanews.ui.listnews.ListNewsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentModule {
    @ContributesAndroidInjector
    fun provide_listNewsFragment():ListNewsFragment
}