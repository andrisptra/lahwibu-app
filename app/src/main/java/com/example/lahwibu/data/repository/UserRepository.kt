package com.example.lahwibu.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.lahwibu.data.response.DataItemOngoing
import com.example.lahwibu.data.retrofit.ApiService
import com.example.lahwibu.utils.AnimeListPagingSource
import com.example.lahwibu.utils.Result

class UserRepository private constructor(
    private val apiService: ApiService
) {

    fun getOngoingAnime(order: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getOngoingAnime(order)
            if (successResponse.data.isEmpty()) {
                emit(Result.Error("Something went wrong"))
            } else {
                emit(Result.Success(successResponse))
            }
        } catch (e: Exception) {
            Log.e(this@UserRepository.toString(), e.message.toString())
        }

    }


    fun getOngoingAnimeAll(order: String): LiveData<PagingData<DataItemOngoing>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                AnimeListPagingSource(apiService, order)
            }
        ).liveData
    }

    fun getCompletedAnime(page: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getCompletedAnime(page)
            if (successResponse.data.isNullOrEmpty()) {
                emit(Result.Error("Something went wrong"))
            } else {
                emit(Result.Success(successResponse))
            }
        } catch (e: Exception) {
            Log.e(this@UserRepository.toString(), e.message.toString())
        }
    }

    fun searchAnime(query: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.searchAnime(query)
            if (successResponse.data.isNullOrEmpty()) {
                emit(Result.Error("item tidak ada"))
            } else {
                emit(Result.Success(successResponse))
            }
        } catch (e: Exception) {
            Log.e(this@UserRepository.toString(), e.message.toString())
        }
    }

    fun getDetailAnime(animeCode: String, id: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getDetailAnime(animeCode, id)
            emit(Result.Success(successResponse))
        } catch (e: Exception) {
            Log.e(this@UserRepository.toString(), e.message.toString())
        }
    }

    fun getDetailEpisode(animeCode: String, id: String, eps: String) = liveData {
        emit(Result.Loading)
        try {
            val successResponse = apiService.getDetailEpisode(animeCode, id, eps)
            emit(Result.Success(successResponse))
        } catch (e: Exception) {
            Log.e(this@UserRepository.toString(), e.message.toString())
        }
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService)
            }.also { instance = it }
    }
}