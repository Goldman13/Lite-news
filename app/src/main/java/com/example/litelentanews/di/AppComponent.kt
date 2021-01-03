package com.example.litelentanews.di

import com.example.litelentanews.MainApplication
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Scope
import javax.inject.Singleton

@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    FragmentModule::class,
    ViewModelModule::class]
)
@Singleton
interface AppComponent: AndroidInjector<MainApplication>{
    @Component.Factory
    interface Factory: AndroidInjector.Factory<MainApplication>

}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FeatureScope