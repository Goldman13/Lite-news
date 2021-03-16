package com.example.litelentanews.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.litelentanews.data.api.DataSource
import com.example.litelentanews.data.api.LentaNews_DataSource
import com.example.litelentanews.data.db.NewsDao
import com.example.litelentanews.data.db.NewsEntity
import com.example.litelentanews.domain.model.Category
import com.example.litelentanews.domain.model.NewsItemModel
import com.example.litelentanews.domain.repository.NewsRepository
import com.example.litelentanews.util.Result
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Provider

class NewsRepositoryImpl @Inject constructor(
        private val map:Map<Class<out DataSource>,@JvmSuppressWildcards Provider<DataSource>>,
        private val dataDB:NewsDao
):NewsRepository<NewsItemModel>() {

    override lateinit var dataSourseClass: Class<out DataSource>
    override val dataSourceInst: DataSource by lazy {
        map.get(dataSourseClass)?.get()?:throw NullPointerException()
    }

    override suspend fun loadNews():Result<*> {
        val result = dataSourceInst.loadRssFile(getButtomDate())
        return if(result is Result.Success){
            result.data?.let { list ->
                dataDB.insert(list = list.map { NewsEntity(it) })
            }
            Result.Success(null)
        }else result
    }

    override fun getNewsListCategory_DB(category: String): LiveData<List<NewsItemModel>> {
        return LiveDataMapper_NewsEntity_to_NewsItemModel(dataDB.queryNewsListCategory(category,dataSourceInst.getConstName()))
    }

    private fun LiveDataMapper_NewsEntity_to_NewsItemModel(
            listLD:LiveData<List<NewsEntity>>
    ):LiveData<List<NewsItemModel>>
    {
        return Transformations.map(listLD){
            it.map {
                NewsItemModel(
                        it.guid,
                        it.title,
                        it.link,
                        it.linkImage,
                        it.category,
                        it.date,
                        it.favorite
                )
            }
        }
    }

    override fun loadDescriptionNews(url: String): String {
        return dataSourceInst.loadHtmlPageNews(url)
    }

    override fun getAllCategory_DB(): LiveData<List<Category>> {
        return dataDB.queryGroupCategory(dataSourceInst.getConstName())
    }

    override fun getAllCompanyNews(): LiveData<List<NewsItemModel>> {
        return LiveDataMapper_NewsEntity_to_NewsItemModel(dataDB.queryAllNews(dataSourceInst.getConstName()))
    }

    override suspend fun deleteOldNews(): Result<*> {
        dataDB.deleteNews(getButtomDate())
        return Result.Success(null)
    }

    private fun getButtomDate():Long{
        return LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .minusDays(3)
            .toInstant()
            .toEpochMilli()
    }

    override suspend fun changeFavorite(guid:String,value: Boolean) {
        dataDB.updateFavorites(guid,value)
    }
}