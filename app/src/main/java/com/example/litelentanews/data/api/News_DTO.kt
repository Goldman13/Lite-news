package com.example.litelentanews.data.api

data class News_DTO(
    val guid: String,
    val title: String,
    val link: String,
    val linkImage:String,
    val category: String,
    val date: Long,
    val nameCompany: String
){
    companion object{
        const val GUID = "guid"
        const val TITLE = "title"
        const val LINK = "link"
        const val LINK_IMAGE = "enclosure"
        const val CATEGORY = "category"
        const val DATE = "pubDate"
        const val NAME = "name"
    }
    constructor(map:Map<String,String>):this(
        guid = map.get(GUID)?:"",
        title = map.get(TITLE)?:"",
        link = map.get(LINK)?:"",
        linkImage = map.get(LINK_IMAGE)?:"",
        category = map.get(CATEGORY)?:"",
        date = (map.get(DATE)?:"").toLong(),
        nameCompany = map.get(NAME)?:""
    )
}