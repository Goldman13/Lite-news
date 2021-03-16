package com.example.litelentanews.ui.categorynews

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.litelentanews.data.api.DataSource
import com.example.litelentanews.domain.model.Category
import com.example.litelentanews.domain.model.NewsItemModel
import com.example.litelentanews.domain.usecase.GetAllCategoryUseCase
import com.example.litelentanews.domain.usecase.UpdateNewsUseCase
import com.example.litelentanews.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.reflect.KClass

class ListNewsCategoriesViewModel @Inject constructor(
        private val getAllCategoryUseCase: GetAllCategoryUseCase,
        private val updateNewsUseCase: UpdateNewsUseCase<Result<*>>
):ViewModel() {
    lateinit var name:String
    private lateinit var classType:Class<out DataSource>

    val list:LiveData<List<Category>> by lazy {
        classType = Class.forName(name) as Class<out DataSource>
        getAllCategoryUseCase(classType)
    }

    fun uploadNews(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){updateNewsUseCase(classType)}
        }
    }
}