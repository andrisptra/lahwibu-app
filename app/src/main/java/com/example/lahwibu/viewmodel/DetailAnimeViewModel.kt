package com.example.lahwibu.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lahwibu.data.repository.UserRepository

class DetailAnimeViewModel(private val repository: UserRepository): ViewModel() {

    fun getDetailAnime(code: String, id:String) = repository.getDetailAnime(code,id)

}