package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.room.entity.Scan
import com.dicoding.asclepius.data.repository.AppRepository
import com.dicoding.asclepius.helper.getFormattedTimestamp
import com.dicoding.asclepius.helper.imageFileToByteArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {
    fun saveScan(label: String, confidenceScore: Int, file: File) {
        viewModelScope.launch {
            val blob = imageFileToByteArray(file)
            repository.saveScanToHistory(
                Scan(
                    label = label,
                    confidenceScore = confidenceScore,
                    scanImgBlob = blob,
                    timestamp = Date().getFormattedTimestamp()
                )
            )
        }
    }
}