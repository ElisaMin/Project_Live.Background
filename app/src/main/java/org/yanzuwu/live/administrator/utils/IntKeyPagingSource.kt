package org.yanzuwu.live.administrator.utils

import androidx.paging.PagingSource
import androidx.paging.PagingState


fun <T:Any> intKeyPagingSource (
    getKey:(state: PagingState<Int, T>)-> Int = {0},
    load: suspend (params: PagingSource.LoadParams<Int>)->PagingSource.LoadResult<Int,T>
) {
    object : PagingSource<Int,T> () {
        override fun getRefreshKey(state: PagingState<Int, T>): Int = getKey(state)
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> = load(params)
    }
}