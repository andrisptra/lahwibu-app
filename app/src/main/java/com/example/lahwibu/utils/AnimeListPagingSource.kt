package com.example.lahwibu.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.lahwibu.data.response.DataItemOngoing
import com.example.lahwibu.data.retrofit.ApiService

class AnimeListPagingSource(private val apiService: ApiService, private val order: String) :
    PagingSource<Int, DataItemOngoing>() {
    override fun getRefreshKey(state: PagingState<Int, DataItemOngoing>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DataItemOngoing> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getOngoingAnimeAll(position,order)
            val data = responseData.data
            val prevKey = responseData.prevPage
            val nextPage = responseData.nextPage
            LoadResult.Page(
                data = data,
                prevKey = if (prevKey == true) position - 1 else null ,
                nextKey = if (nextPage == true) position + 1 else null
            )

        } catch (e: Exception) {
            return LoadResult.Error(e)

        }
    }


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}