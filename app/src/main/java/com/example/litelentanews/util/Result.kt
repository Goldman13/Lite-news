package com.example.litelentanews.util

import com.example.litelentanews.data.api.LentaNews_DTO
import com.example.litelentanews.data.db.LentaNewsEntity
import com.example.litelentanews.domain.model.LentaNewsItem

sealed class Result<T>{
    data class Success<T>(val data:T?):Result<T>()
    data class Error<T>(val message: String):Result<T>()
    class Load():Result<Any>()
}

