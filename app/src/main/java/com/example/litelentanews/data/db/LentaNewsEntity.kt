package com.example.litelentanews.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.litelentanews.data.api.LentaNews_DTO

@Entity()
data class LentaNewsEntity(
        @PrimaryKey
        val guid: String,
        val title:String,
        val link:String,
        val linkImage:String,
        val category:String,
        val date:Long
){
        constructor(item: LentaNews_DTO):this(
                guid = item.guid,
                title = item.title,
                link = item.link,
                linkImage = item.linkImage,
                category = item.category,
                date = item.date

        )
}