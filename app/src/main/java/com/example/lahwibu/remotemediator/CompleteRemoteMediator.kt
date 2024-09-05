package com.example.lahwibu.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.lahwibu.data.database.LahWibuDatabase
import com.example.lahwibu.data.database.entity.RemoteKeysComplete
import com.example.lahwibu.data.database.entity.RemoteKeysOngoing
import com.example.lahwibu.data.response.DataItemCompleted
import com.example.lahwibu.data.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class CompleteRemoteMediator(
    private val database: LahWibuDatabase,
    private val apiService: ApiService,
    private val order: String
) : RemoteMediator<Int, DataItemCompleted>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DataItemCompleted>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosesToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeysForFirstItem(state)
                val prevKeys = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKeys
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeysForLastItem(state)
                val nextKeys = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKeys
            }
        }

        return try {
            val responseData = apiService.getCompleteAnimeAll(page, order)
            val endOfPaginationReached = responseData.data.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeysComplete()
                    database.animeDao().deleteAllCompleted()
                }
                val prevKey = if (page == 1) null else page - 1
                val nexKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.data.map {
                    RemoteKeysComplete(id = it.animeId, prevKey, nexKey)
                }
                database.remoteKeysDao().insertAllComplete(keys)
                database.animeDao().insertCompleteAnime(responseData.data)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, DataItemCompleted>): RemoteKeysComplete? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysIdComplete(data.animeId)
        }
    }

    private suspend fun getRemoteKeysForFirstItem(state: PagingState<Int, DataItemCompleted>): RemoteKeysComplete? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysIdComplete(data.animeId)

        }
    }

    private suspend fun getRemoteKeyClosesToCurrentPosition(state: PagingState<Int, DataItemCompleted>): RemoteKeysComplete? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.animeId?.let { id ->
                database.remoteKeysDao().getRemoteKeysIdComplete(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}