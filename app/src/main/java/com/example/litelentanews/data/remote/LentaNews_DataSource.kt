package com.example.litelentanews.data.remote


import com.example.litelentanews.data.api.LentaNews_DTO
import com.example.litelentanews.util.Result
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.intellij.lang.annotations.RegExp
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URI
import java.net.URL
import java.time.*
import java.time.Month.*
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class LentaNews_DataSource @Inject constructor(
    private val httpClient: OkHttpClient
) {
    companion object{
        const val base_url = "https://m.lenta.ru"
    }
    private lateinit var listNews:MutableList<LentaNews_DTO>

    fun loadRssFile():Result<List<LentaNews_DTO>>{
        listNews = mutableListOf()
        try {
            httpClient.newCall(
                Request.Builder()
                    .url(base_url+"/rss/news")
                    .build()
            ).execute().use { response ->
                handleRssFile(response)
            }
        }catch (e:Exception){
            return Result.Error(e.message?:"error")
        }
        return Result.Success(listNews)
    }

    fun handleRssFile(response:Response){
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(response.body()?.charStream())
        var eventType = parser.getEventType()
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.name=="item" && eventType == XmlPullParser.START_TAG) {
                parser.next()
                val tempMap = mutableMapOf<String,String>()
                while(parser.name!="item"){
                    when(parser.name){
                        LentaNews_DTO.GUID -> tempMap.put(parser.name, parser.nextText())
                        LentaNews_DTO.TITLE -> tempMap.put(parser.name, parser.nextText())
                        LentaNews_DTO.LINK  -> {
                            val link = parser.nextText()
                            val uri = URI.create(link)
                            tempMap.put(parser.name, uri.scheme+"://m."+uri.host + uri.path)
                        }
                        LentaNews_DTO.LINK_IMAGE  -> tempMap.put(parser.name, parser.getAttributeValue(0))
                        LentaNews_DTO.CATEGORY -> tempMap.put(parser.name, parser.nextText())
                        LentaNews_DTO.DATE -> tempMap.put(parser.name, parseDate(parser.nextText()).toString())
                    }
                    parser.next()
                }
                listNews.add(LentaNews_DTO(tempMap))
            }
            eventType = parser.next()
        }
    }

    private fun parseDate(date:String):Long{
        val parseDatelist = date.substringAfter(",").trim().split(" ")
        var numberMonth = "01"
        val regex = Regex("^${parseDatelist[1].toUpperCase(Locale.ROOT)}*")
        for (item in Month.values()){
            if(regex.containsMatchIn(item.name)){
                numberMonth = if(item.value<10) "0"+item.value else ""+item.value
                break
            }
        }
        //Instant.ofEpochMilli(mill).atZone(ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")
        return ZonedDateTime.of(LocalDateTime.parse(
                parseDatelist[2]+numberMonth+parseDatelist[0]+" "+parseDatelist[3],
                formatter),
                ZoneId.of(parseDatelist[4])
        ).toInstant().toEpochMilli()
    }
}