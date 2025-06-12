package com.skytoph.taski.presentation.core

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class AbstractPagingSource<T : Any> : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: 0

        return try {
            withContext(Dispatchers.IO) {
                val data = getData(page)
                LoadResult.Page(
                    data = listOf(data),
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (data.isEmpty()) null else page + 1
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    abstract suspend fun getData(page: Int): T

    abstract fun T.isEmpty(): Boolean

    override fun getRefreshKey(state: PagingState<Int, T>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
}