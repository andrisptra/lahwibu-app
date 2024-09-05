package com.example.lahwibu.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lahwibu.data.repository.UserRepository

class EpisodeDetailViewModel(private val repository: UserRepository) : ViewModel() {
    fun getDetailEpisode(code: String, id: String, eps: String) =
        repository.getDetailEpisode(code, id, eps)

    fun getDetailAnime(code: String, id:String) = repository.getDetailAnime(code,id)
}