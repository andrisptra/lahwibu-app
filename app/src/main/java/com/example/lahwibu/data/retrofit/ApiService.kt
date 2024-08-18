package com.example.lahwibu.data.retrofit

import com.example.lahwibu.data.response.CompletedAnimeResponse
import com.example.lahwibu.data.response.OngoingAnimeResponse
import com.example.lahwibu.data.response.SearchAnimeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("anime/search")
    suspend fun searchAnime(
        @Query("query") query: String
    ): SearchAnimeResponse

    @GET("anime/ongoing")
    suspend fun getOngoingAnime(
        @Query("page") page: String
    ): OngoingAnimeResponse

    @GET("anime/finished")
    suspend fun getCompletedAnime(
        @Query("page") page: String
    ): CompletedAnimeResponse
}