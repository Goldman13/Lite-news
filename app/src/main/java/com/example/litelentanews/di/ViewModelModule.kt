package com.example.litelentanews.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.litelentanews.ui.ViewModelFactory
import com.example.litelentanews.ui.listnews.ListNewsViewModel
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
    @ViewModelKey(ListNewsViewModel::class)
    fun bindListNewsViewModel(viewModel: ListNewsViewModel):ViewModel
}