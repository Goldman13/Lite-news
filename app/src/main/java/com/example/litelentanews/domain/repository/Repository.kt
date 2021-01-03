package com.example.litelentanews.domain.repository

import androidx.lifecycle.LiveData
import com.example.litelentanews.data.api.LentaNews_DTO
import com.example.litelentanews.data.db.LentaNewsDao
import com.example.litelentanews.data.db.LentaNewsEntity
import com.example.litelentanews.data.remote.LentaNews_DataSource
import com.example.litelentanews.domain.model.LentaNewsItem
import com.example.litelentanews.util.Result
import javax.inject.Inject

class Repository @Inject constructor(
    private val dataRemote:LentaNews_DataSource,
    private val db_Dao: LentaNewsDao
) {
    suspend fun loadRssList():Result<*>{
       var result = dataRemote.loadRssFile()
       if(result is Result.Success<List<LentaNews_DTO>>){
           result.data?.let {
               db_Dao.insert(it.map{LentaNewsEntity(it)})
           }
           result = Result.Success(null)
       }
       return result
    }

    fun loadRssList_Db():LiveData<List<LentaNewsEntity>>{
       return db_Dao.queryAllNews()
    }
}