package com.example.litelentanews.di

import android.content.Context
import androidx.room.Room
import com.example.litelentanews.MainApplication
import com.example.litelentanews.data.db.NewsDao
import com.example.litelentanews.data.db.NewsDataBase
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule{

    @Singleton
    @Provides
    fun provide_appContext(app: MainApplication): Context {
        return app.applicationContext
    }

    @Singleton
    @Provides
    @Named("NoCache")
    fun provide_httpClient_NoCache():OkHttpClient{
       return OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    @Named("Cache")
    fun provide_httpClient(appContext: Context):OkHttpClient{
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        return OkHttpClient.Builder()
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
            .build()
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
    fun provide_News_Dao(newsDb:NewsDataBase):NewsDao{
        return newsDb.NewsDao()
    }
}