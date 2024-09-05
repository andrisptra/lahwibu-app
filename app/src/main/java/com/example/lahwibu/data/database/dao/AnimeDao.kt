package com.example.lahwibu.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lahwibu.data.response.DataItemCompleted
import com.example.lahwibu.data.response.DataItemOngoing

@Dao
interface AnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOngoingAnime(anime: List<DataItemOngoing>)

    @Query("SELECT * FROM ongoing_list")
    fun getAllOngoingAnime(): PagingSource<Int, DataItemOngoing>

    @Query("DELETE FROM ongoing_list")
    suspend fun deleteAllOngoing()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompleteAnime(anime: List<DataItemCompleted>)

    @Query("SELECT * FROM complete_list")
    fun getAllCompletedAnime(): PagingSource<Int, DataItemCompleted>

    @Query("DELETE FROM complete_list")
    suspend fun deleteAllCompleted()
}