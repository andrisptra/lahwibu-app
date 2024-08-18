package com.example.lahwibu.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lahwibu.data.repository.UserRepository

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    fun getOngoingAnime(page:String) = repository.getOngoingAnime(page)
    fun getCompletedAnime(page: String) = repository.getCompletedAnime(page)

}