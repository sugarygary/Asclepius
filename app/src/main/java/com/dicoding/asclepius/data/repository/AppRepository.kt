package com.dicoding.asclepius.data.repository

import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.local.room.entity.Scan
import com.dicoding.asclepius.data.network.response.Article

interface AppRepository {
    suspend fun fetchCancerHeadlines(): LiveData<Async<List<Article>>>
    suspend fun fetchScanHistory(handleLoading: Boolean): LiveData<Async<List<Scan>>>
    suspend fun saveScanToHistory(scan: Scan)
    suspend fun deleteScanHistory(scan: Scan)
}