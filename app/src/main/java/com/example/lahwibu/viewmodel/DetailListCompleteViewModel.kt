package com.example.lahwibu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.lahwibu.data.repository.UserRepository
import com.example.lahwibu.data.response.DataItemCompleted
import com.example.lahwibu.data.response.DataItemOngoing

class DetailListCompleteViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getAllCompleteList(order:String): LiveData<PagingData<DataItemCompleted>> =
        userRepository.getAllCompleteAnime(order).cachedIn(viewModelScope)

}