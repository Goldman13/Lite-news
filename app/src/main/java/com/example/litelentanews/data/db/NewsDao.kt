package com.example.litelentanews.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.litelentanews.domain.model.Category
import retrofit2.http.DELETE

@Dao
interface NewsDao {
    @Query("DELETE FROM NewsEntity WHERE date<=:date")
    suspend fun deleteNews(date:Long)

    @Query("SELECT * FROM NewsEntity WHERE nameCompany=:nameCompany ORDER BY date DESC")
    fun queryAllNews(nameCompany: String): LiveData<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(list:List<NewsEntity>)

    @Query("SELECT category as name, count(category) as total FROM NewsEntity WHERE nameCompany=:nameCompany GROUP BY category")
    fun queryGroupCategory(nameCompany: String):LiveData<List<Category>>

    @Query("SELECT * FROM NewsEntity WHERE category=:category AND nameCompany=:companyName ORDER BY date DESC")
    fun queryNewsListCategory(category:String, companyName:String):LiveData<List<NewsEntity>>

    @Query("SELECT favorite FROM NewsEntity WHERE guid=:guid")
    suspend fun queryIsFavorites(guid:String):Boolean

    @Query("UPDATE NewsEntity SET favorite=:value WHERE guid=:guid")
    suspend fun updateFavorites(guid:String,value: Boolean)
}