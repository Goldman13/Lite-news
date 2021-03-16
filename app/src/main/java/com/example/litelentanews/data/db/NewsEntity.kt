package com.example.litelentanews.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.litelentanews.data.api.News_DTO

@Entity()
data class NewsEntity(
        @PrimaryKey
        val guid: String,
        val title:String,
        val link:String,
        val linkImage:String,
        val category:String,
        val date:Long,
        val nameCompany:String,
        val favorite:Boolean
){
        constructor(item: News_DTO):this(
                guid = item.guid,
                title = item.title,
                link = item.link,
                linkImage = item.linkImage,
                category = item.category,
                date = item.date,
                nameCompany = item.nameCompany,
                favorite = false
        )
}