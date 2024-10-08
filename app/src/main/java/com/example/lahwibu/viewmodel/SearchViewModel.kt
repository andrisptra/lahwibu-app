package com.example.lahwibu.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lahwibu.data.repository.UserRepository

class SearchViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun searchAnime(query: String) = userRepository.searchAnime(query)
}