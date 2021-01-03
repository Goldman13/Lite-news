package com.example.litelentanews.ui.listnews

import android.content.Context
import android.media.audiofx.AudioEffect
import androidx.lifecycle.*
import com.example.litelentanews.data.db.LentaNewsEntity
import com.example.litelentanews.domain.GetLentaRssUseCase
import com.example.litelentanews.domain.model.LentaNewsItem
import com.example.litelentanews.util.Result
import kotlinx.coroutines.*
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ListNewsViewModel @Inject constructor(
        private val useCase:GetLentaRssUseCase,
        private val httpClient: OkHttpClient,
        private val appContext: Context
):ViewModel() {

    private val _status = MutableLiveData<Result<*>>()
    val status:LiveData<Result<*>> = _status

    //private val _listNews = MutableLiveData<List<LentaNewsItem>>()
    lateinit var listNews:LiveData<List<LentaNewsItem>>

    init{
        getRssList_db()
    }

    fun loadRssList(){
        viewModelScope.launch {
            _status.value = withContext(Dispatchers.IO){
                useCase.getRssList()
            }
        }
    }

    private fun getRssList_db(){
        viewModelScope.launch {
            listNews = Transformations.map(useCase.repo.loadRssList_Db()) {
                it.map {
                    LentaNewsItem(
                            guid = it.guid,
                            title = it.title,
                            link = it.link,
                            linkImage = it.linkImage,
                            category = it.category,
                            date = it.date)
                }
            }
        }
    }

    suspend fun getDescriptionNewsItem(url:String) = withContext(Dispatchers.IO){
        try{
            httpClient.newCall(Request.Builder()
                    .url(url).build()
            ).execute().body().use { it?.string()?:"" }
        }catch (e:Exception){
            e.printStackTrace()
            ""
        }
    }
}