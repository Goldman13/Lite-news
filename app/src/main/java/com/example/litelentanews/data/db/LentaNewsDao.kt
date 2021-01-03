package com.example.litelentanews.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.litelentanews.data.api.LentaNews_DTO
import com.example.litelentanews.domain.model.LentaNewsItem

@Dao
interface LentaNewsDao {
    @Query("SELECT * FROM LentaNewsEntity ORDER BY date DESC")
    fun queryAllNews(): LiveData<List<LentaNewsEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(list:List<LentaNewsEntity>)
}