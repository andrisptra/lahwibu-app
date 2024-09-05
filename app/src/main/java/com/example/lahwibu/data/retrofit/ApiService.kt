package com.example.lahwibu.data.retrofit

import com.example.lahwibu.data.response.CompletedAnimeResponse
import com.example.lahwibu.data.response.DetailAnimeResponse
import com.example.lahwibu.data.response.DetailEpisodeResponse
import com.example.lahwibu.data.response.OngoingAnimeResponse
import com.example.lahwibu.data.response.SearchAnimeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("anime/search")
    suspend fun searchAnime(
        @Query("query") query: String

    ): SearchAnimeResponse

    @GET("anime/ongoing")
    suspend fun getOngoingAnime(
        @Query("order_by") order: String
    ): OngoingAnimeResponse

    @GET("anime/ongoing")
    suspend fun getOngoingAnimeAll(
        @Query("page") page: Int,
        @Query("order_by") order: String
    ): OngoingAnimeResponse

    @GET("anime/finished")
    suspend fun getCompleteAnimeAll(
        @Query("page") page: Int,
        @Query("order_by") order: String
    ): CompletedAnimeResponse

    @GET("anime/finished")
    suspend fun getCompletedAnime(
        @Query("order_by") order: String
    ): CompletedAnimeResponse

    @GET("anime/{code}/{id}")
    suspend fun getDetailAnime(
        @Path("code") code: String,
        @Path("id") id: String
    ): DetailAnimeResponse

    @GET("anime/{code}/{id}/episode/{eps}")
    suspend fun getDetailEpisode(
        @Path("code") code: String,
        @Path("id") id: String,
        @Path("eps") eps: String
    ): DetailEpisodeResponse

}