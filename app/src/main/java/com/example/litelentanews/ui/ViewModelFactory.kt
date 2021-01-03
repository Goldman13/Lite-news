package com.example.litelentanews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Suppress("UNCHECKED_CAST")
@Singleton
class ViewModelFactory @Inject constructor(
    private val viewModelMap: Map<Class<out ViewModel>,@JvmSuppressWildcards Provider<ViewModel>>
    ):ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = viewModelMap[modelClass]
                ?:throw IllegalArgumentException("Unknown model class $modelClass")
        return viewModel.get() as T
    }
}