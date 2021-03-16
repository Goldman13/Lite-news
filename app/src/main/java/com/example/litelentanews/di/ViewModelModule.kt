package com.example.litelentanews.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.litelentanews.ui.ViewModelFactory
import com.example.litelentanews.ui.categorynews.ListNewsCategoriesViewModel
import com.example.litelentanews.ui.listnews.NewsViewModel
import com.example.litelentanews.ui.start.StartViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
interface ViewModelModule{

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory):ViewModelProvider.Factory

    @IntoMap
    @Binds
    @ViewModelKey(NewsViewModel::class)
    fun bindListNewsViewModel(viewModel: NewsViewModel):ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(ListNewsCategoriesViewModel::class)
    fun bindListNewsCategoriesViewModel(viewModel: ListNewsCategoriesViewModel):ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(StartViewModel::class)
    fun bindStartViewModel(viewModel: StartViewModel):ViewModel
}