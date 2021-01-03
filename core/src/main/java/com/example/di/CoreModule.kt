package com.example.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.jaxb.JaxbConverterFactory
import javax.inject.Singleton
import javax.xml.bind.JAXB
import javax.xml.bind.JAXBContextFactory
import javax.xml.bind.util.JAXBSource

class CoreModule {
    fun provide_Retrofit(): Retrofit.Builder {
        return Retrofit.Builder()
                .baseUrl("https://lenta.ru/rss")
                .addConverterFactory(JaxbConverterFactory.create())
    }
}