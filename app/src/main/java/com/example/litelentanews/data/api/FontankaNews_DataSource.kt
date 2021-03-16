package com.example.litelentanews.data.api


import com.example.litelentanews.util.Result
import com.example.litelentanews.util.buildCustomUri
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URI
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class FontankaNews_DataSource @Inject constructor(
    @Named("NoCache") private val httpClient: OkHttpClient,
    @Named("Cache") private val httpClient_cache: OkHttpClient
):DataSource {
    companion object{
        const val base_url = "https://m.fontanka.ru"
        const val nameNewsCompany = "Фонтанка"
    }

    val constAccess = Companion
    private lateinit var listNews:MutableList<News_DTO>

    override fun loadRssFile(beginDate:Long):Result<List<News_DTO>>{
        listNews = mutableListOf()
        try {
            httpClient.newCall(
                Request.Builder()
                    .url(base_url +"/fontanka.rss")
                    .build()
            ).execute().use { response ->
                handleRssFile(response,beginDate)
            }
        }catch (e:Exception){
            return Result.Error(e.message?:"error")
        }
        return Result.Success(listNews)
    }

    override fun loadHtmlPageNews(url:String):String{
        return try{
            httpClient_cache.newCall(Request.Builder()
                .url(url).build()
            ).execute().body().use { (it?.string()?:"") }
        }catch (e:Exception){
            e.printStackTrace()
            ""
        }
    }

    override fun getConstName(): String {
        return nameNewsCompany
    }

    override fun handleRssFile(response:Response, beginDate:Long){
        var stopParse = false
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(response.body()?.charStream())
        var eventType = parser.getEventType()
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.name=="item" && eventType == XmlPullParser.START_TAG) {
                parser.next()
                val tempMap = mutableMapOf<String,String>()
                tempMap.put(News_DTO.NAME, nameNewsCompany)
                while(parser.name!="item"){
                    when(parser.name){
                        News_DTO.GUID -> tempMap.put(parser.name, parser.nextText())
                        News_DTO.TITLE -> tempMap.put(parser.name, parser.nextText())
                        News_DTO.LINK  -> {
                            val link = parser.nextText()
                            val uri = URI.create(link)
                            tempMap.put(parser.name, uri.buildCustomUri())
                        }
                        News_DTO.LINK_IMAGE  -> {
                            tempMap.put(parser.name, parser.getAttributeValue(0))
                            break
                        }
                        News_DTO.CATEGORY -> {tempMap.put(parser.name, parser.nextText()) }
                        News_DTO.DATE -> {
                            val newsDate = parseDate(parser.nextText())
                            if(newsDate>beginDate)
                                tempMap.put(parser.name, newsDate.toString())
                            else{
                                stopParse = true
                                break
                        }
                        }
                    }
                    parser.next()
                }
                if(stopParse) break
                listNews.add(News_DTO(tempMap))
            }
            eventType = parser.next()
        }
    }

    override fun parseDate(date:String):Long{
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