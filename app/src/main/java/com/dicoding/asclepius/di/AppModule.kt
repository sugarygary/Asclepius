package com.dicoding.asclepius.di

import android.app.Application
import androidx.room.Room
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.local.room.database.AsclepiusDatabase
import com.dicoding.asclepius.data.network.service.NewsApiService
import com.dicoding.asclepius.data.repository.AppRepository
import com.dicoding.asclepius.data.repository.AppRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesNewsApi(): NewsApiService {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
            .create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesDatabase(application: Application): AsclepiusDatabase {
        return Room
            .databaseBuilder(
                application, AsclepiusDatabase::class.java, "Asclepius.db"
            )
            .build()
    }

    @Provides
    @Singleton
    fun providesAppRepository(
        newsApi: NewsApiService, database: AsclepiusDatabase
    ): AppRepository {
        return AppRepositoryImpl(newsApi, database)
    }
}
