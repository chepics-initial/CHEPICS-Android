package com.chepics.chepics.repository.search

import com.chepics.chepics.common.di.IoDispatcher
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface SearchRepository {
    suspend fun fetchSearchedTopics(word: String): CallResult<List<Topic>>
    suspend fun fetchSearchedComments(word: String): CallResult<List<Comment>>
    suspend fun fetchSearchedUsers(word: String): CallResult<List<User>>
}

internal class SearchRepositoryImpl @Inject constructor(
    private val searchDataSource: SearchDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): SearchRepository {
    override suspend fun fetchSearchedTopics(word: String): CallResult<List<Topic>> {
        return withContext(ioDispatcher) {
            searchDataSource.fetchSearchedTopics(word)
        }
    }

    override suspend fun fetchSearchedComments(word: String): CallResult<List<Comment>> {
        return withContext(ioDispatcher) {
            searchDataSource.fetchSearchedComments(word)
        }
    }

    override suspend fun fetchSearchedUsers(word: String): CallResult<List<User>> {
        return withContext(ioDispatcher) {
            searchDataSource.fetchSearchedUsers(word)
        }
    }

}