package com.example.litelentanews.domain.model

data class NewsItemModel(
    val guid:String,
    val title:String,
    val link:String,
    val linkImage:String,
    val category:String,
    val date:Long,
    val favorite:Boolean
)