package com.dicoding.asclepius.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.local.room.database.AsclepiusDatabase
import com.dicoding.asclepius.data.local.room.entity.Scan
import com.dicoding.asclepius.data.network.service.NewsApiService
import com.dicoding.asclepius.data.network.response.Article

class AppRepositoryImpl(
    private val newsApi: NewsApiService,
    private val database: AsclepiusDatabase
) : AppRepository {

    override suspend fun fetchCancerHeadlines(): LiveData<Async<List<Article>>> =
        liveData {
            emit(Async.Loading)
            try {
                val response = newsApi.getHeadlines(apiKey = BuildConfig.API_KEY)
                if (response.articles.isEmpty() || response.totalResults == 0) {
                    emit(Async.Empty)
                } else {
                    emit(Async.Success(response.articles.filter { article -> article.title != "[Removed]" }))
                }
            } catch (e: Exception) {
                emit(Async.Error(e.message.toString()))
            }
        }

    override suspend fun fetchScanHistory(handleLoading: Boolean): LiveData<Async<List<Scan>>> =
        liveData {
            if (handleLoading) {
                emit(Async.Loading)
            }
            try {
                val history = database.scanDao().getALl()
                if (history.isEmpty()) {
                    emit(Async.Empty)
                } else {
                    emit(Async.Success(history))
                }
            } catch (e: Exception) {
                emit(Async.Error(e.message.toString()))
            }
        }

    override suspend fun saveScanToHistory(scan: Scan) {
        database.scanDao().insertScan(scan)
    }

    override suspend fun deleteScanHistory(scan: Scan) {
        database.scanDao().deleteScan(scan)
    }

}

