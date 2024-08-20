package com.example.lahwibu.ui.episode

import androidx.lifecycle.ViewModel
import com.example.lahwibu.data.repository.UserRepository

class EpisodeDetailViewModel(private val repository: UserRepository) : ViewModel() {
    fun getDetailEpisode(code: String, id: String, eps: String) =
        repository.getDetailEpisode(code, id, eps)
}