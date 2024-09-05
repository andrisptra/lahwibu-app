package com.example.lahwibu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.lahwibu.data.repository.UserRepository
import com.example.lahwibu.data.response.DataItemOngoing

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    fun getOngoingAnime(order: String) = repository.getOngoingAnime(order)
    fun getCompletedAnime(order: String) = repository.getCompletedAnime(order)

}