package com.example.litelentanews.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        LentaNewsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NewsDataBase:RoomDatabase() {
    abstract fun lentaNewsDao(): LentaNewsDao

}