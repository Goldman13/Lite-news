package com.example.litelentanews.di

import android.content.Context
import android.content.res.Resources
import android.os.Environment
import androidx.room.Room
import com.example.litelentanews.MainApplication
import com.example.litelentanews.R
import com.example.litelentanews.data.api.LentaNewsService
import com.example.litelentanews.data.db.LentaNewsDao
import com.example.litelentanews.data.db.NewsDataBase
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

@Module
class AppModule{

    @Singleton
    @Provides
    fun appContext(app: MainApplication): Context {
        return app.applicationContext
    }

    @Singleton
    @Provides
    fun getHttpClient(appContext: Context):OkHttpClient{
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return OkHttpClient.Builder()
                //TODO("Удалить после отладки")
//            .addInterceptor(object:Interceptor{
//                override fun intercept(chain: Interceptor.Chain): Response {
//                    val response = chain.proceed(chain.request())
//                    return response.newBuilder().body(ResponseBody.create(
//                            response.body()?.contentType(),
//                            app.resources.openRawResource(R.raw.test_xml).readBytes().decodeToString())
//                    ).build()
//                }
//            })
                .addNetworkInterceptor(object:Interceptor{
                    override fun intercept(chain: Interceptor.Chain): Response {
                        val response = chain.proceed(chain.request())
                        val cacheControl = CacheControl.Builder()
                            .maxAge(31536000, TimeUnit.SECONDS)
                            .build()

                        return response.newBuilder()
                            .removeHeader("Pragma")
                            .removeHeader("Cache-Control")
                            .header("Cache-Control", cacheControl.toString())
                            .build()
                }
            })
            .cache(Cache(appContext.externalCacheDir, 1024 * 1024 * 10))
            //.addInterceptor(logging)
            .build()
    }

    @Singleton
    @Provides
    fun provide_Retrofit(client: OkHttpClient):Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://lenta.ru")
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provide_lentaNewsService(retrofit: Retrofit):LentaNewsService{
       return retrofit.create(LentaNewsService::class.java)
    }

    @Singleton
    @Provides
    fun provide_dbBase(appContext:Context): NewsDataBase{
        return Room.databaseBuilder(
            appContext,
            NewsDataBase::class.java,
            "news_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provide_lentaNews_Dao(newsDb:NewsDataBase):LentaNewsDao{
        return newsDb.lentaNewsDao()
    }

}