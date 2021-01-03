package com.example.litelentanews.domain.model

data class LentaNewsItem(
    val guid:String,
    val title:String,
    val link:String,
    val linkImage:String,
    val category:String,
    val date:Long
)