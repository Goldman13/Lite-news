package com.example.litelentanews.ui.listnews

import android.content.Context
import androidx.lifecycle.*
import com.example.litelentanews.data.api.DataSource
import com.example.litelentanews.domain.GetNewsListCategoryUseCase
import com.example.litelentanews.domain.GetNewsListCategoryUseCaseImpl
import com.example.litelentanews.domain.model.NewsItemModel
import com.example.litelentanews.domain.usecase.ChangeFavoriteStatusNewsUseCase
import com.example.litelentanews.domain.usecase.GetAllCompanyNewsUseCase
import com.example.litelentanews.domain.usecase.GetNewsDescriptionUseCase
import com.example.litelentanews.domain.usecase.UpdateNewsUseCase
import com.example.litelentanews.util.Result
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class NewsViewModel @Inject constructor(
        private val updateNewsUseCase:UpdateNewsUseCase<Result<*>>,
        private val getAllCompanyNewsUseCase: GetAllCompanyNewsUseCase<NewsItemModel>,
        private val getNewsListCategoryUseCase: GetNewsListCategoryUseCase<NewsItemModel>,
        private val getNewsDescriptionUseCase: GetNewsDescriptionUseCase,
        private val changeFavoriteStatusNewsUseCase: ChangeFavoriteStatusNewsUseCase
):ViewModel() {

    private val _status = MutableLiveData<Result<*>>()
    val status:LiveData<Result<*>> = _status

    var queryNewsList = MediatorLiveData<List<NewsItemModel>>()
    val listNews = MutableLiveData<List<NewsItemModel>>()

    private lateinit var classType:Class<out DataSource>

    fun addSource(categoryName:String, className: String){
        classType = Class.forName(className) as Class<out DataSource>
        if(categoryName.isEmpty())
            queryNewsList.addSource(getAllCompanyNewsUseCase(classType)){
                queryNewsList.value = it
            }
        else
            queryNewsList.addSource(getNewsListCategoryUseCase(classType, categoryName)){
                queryNewsList.value = it
            }
    }

    fun uploadNews(){
        viewModelScope.launch {
            _status.value = withContext(Dispatchers.IO){updateNewsUseCase(classType)}
        }
    }

    suspend fun getDescriptionNewsItem(url:String) = withContext(Dispatchers.IO){
        getNewsDescriptionUseCase(url, classType)
    }

    fun changeFavorite(guid:String, value:Boolean){
        viewModelScope.launch {
            changeFavoriteStatusNewsUseCase.invoke(guid,value)
        }
    }
}