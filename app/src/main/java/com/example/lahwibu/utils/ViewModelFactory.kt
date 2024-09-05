package com.example.lahwibu.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lahwibu.data.di.Injection
import com.example.lahwibu.data.repository.UserRepository
import com.example.lahwibu.viewmodel.DetailAnimeViewModel
import com.example.lahwibu.viewmodel.DetailListCompleteViewModel
import com.example.lahwibu.viewmodel.DetailListOngoingViewModel
import com.example.lahwibu.viewmodel.EpisodeDetailViewModel
import com.example.lahwibu.viewmodel.HomeViewModel
import com.example.lahwibu.viewmodel.SearchViewModel

class ViewModelFactory private constructor(private val mRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            HomeViewModel(mRepository) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            SearchViewModel(mRepository) as T
        } else if (modelClass.isAssignableFrom(DetailAnimeViewModel::class.java)) {
            DetailAnimeViewModel(mRepository) as T
        } else if (modelClass.isAssignableFrom(EpisodeDetailViewModel::class.java)) {
            EpisodeDetailViewModel(mRepository) as T
        } else if (modelClass.isAssignableFrom(DetailListOngoingViewModel::class.java)) {
            DetailListOngoingViewModel(mRepository) as T
        } else if (modelClass.isAssignableFrom(DetailListCompleteViewModel::class.java)) {
            DetailListCompleteViewModel(mRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}