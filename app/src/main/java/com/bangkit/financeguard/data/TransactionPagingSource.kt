package com.bangkit.financeguard.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bangkit.financeguard.data.pref.UserPreference
import com.bangkit.financeguard.data.remote.response.ListTransactionItem
import com.bangkit.financeguard.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first

class TransactionPagingSource(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) : PagingSource<Int, ListTransactionItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListTransactionItem> {
        return try {
            val position = params.key ?: 1
            val user = userPreference.getSession().first()
            val authorization = "Bearer ${user.accessToken}"
            val response = apiService.getTransactions(authorization, position, params.loadSize)
            LoadResult.Page(
                data = response.data,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (response.data.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListTransactionItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
