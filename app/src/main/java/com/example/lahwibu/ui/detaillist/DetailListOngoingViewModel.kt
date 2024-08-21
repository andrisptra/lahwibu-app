package com.example.lahwibu.ui.detaillist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.lahwibu.data.repository.UserRepository
import com.example.lahwibu.data.response.DataItemOngoing

class DetailListOngoingViewModel(private val userRepository: UserRepository):ViewModel() {
    fun getAllOngoingList(order:String): LiveData<PagingData<DataItemOngoing>> =
        userRepository.getOngoingAnimeAll(order).cachedIn(viewModelScope)

    fun getOngoingList(order: String) = userRepository.getOngoingAnime(order)
}