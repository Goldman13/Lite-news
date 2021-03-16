package com.example.litelentanews.di

import androidx.lifecycle.ViewModel
import com.example.litelentanews.data.api.DataSource
import com.example.litelentanews.data.api.FontankaNews_DataSource
import com.example.litelentanews.data.api.LentaNews_DataSource
import com.example.litelentanews.data.api.Nplus1News_DataSource
import com.example.litelentanews.ui.listnews.NewsViewModel
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
internal annotation class DataSourceKey(val value: KClass<out DataSource>)

@Module
interface DataSourceModule {
    @IntoMap
    @Binds
    @DataSourceKey(LentaNews_DataSource::class)
    fun bindLentaNews_DataSource(dataSource: LentaNews_DataSource):DataSource

    @IntoMap
    @Binds
    @DataSourceKey(FontankaNews_DataSource::class)
    fun bindFontankaNews_DataSource(dataSource: FontankaNews_DataSource):DataSource

    @IntoMap
    @Binds
    @DataSourceKey(Nplus1News_DataSource::class)
    fun bindNplus1News_DataSource(dataSource: Nplus1News_DataSource):DataSource
}