package com.example.lahwibu.data.di

import android.content.Context
import com.example.lahwibu.data.database.LahWibuDatabase
import com.example.lahwibu.data.repository.UserRepository
import com.example.lahwibu.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = LahWibuDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()

        return UserRepository.getInstance(database,apiService)
    }
}