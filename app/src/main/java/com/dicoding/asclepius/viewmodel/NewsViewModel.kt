package com.dicoding.asclepius.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.repository.Async
import com.dicoding.asclepius.data.network.response.Article
import com.dicoding.asclepius.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: AppRepository) : ViewModel() {
    private val _articles = MutableLiveData<Async<List<Article>>>()
    val articles: LiveData<Async<List<Article>>>
        get() = _articles

    fun fetchHeadlines() {
        viewModelScope.launch {
            repository.fetchCancerHeadlines().asFlow().collect {
                _articles.postValue(it)
            }
        }
    }
}