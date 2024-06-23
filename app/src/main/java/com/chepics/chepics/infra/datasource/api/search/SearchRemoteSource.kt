package com.chepics.chepics.infra.datasource.api.search

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.infra.datasource.api.safeApiCall
import com.chepics.chepics.repository.search.SearchDataSource
import javax.inject.Inject

class SearchRemoteSource @Inject constructor(private val api: SearchApi) : SearchDataSource {
    override suspend fun fetchSearchedTopics(word: String, offset: Int?): CallResult<List<Topic>> {
        return safeApiCall {
            api.fetchSearchedTopics(
                word = word,
                offset = offset
            )
        }.mapSuccess { it.items }
    }

    override suspend fun fetchSearchedComments(
        word: String,
        offset: Int?
    ): CallResult<List<Comment>> {
        return safeApiCall {
            api.fetchSearchedComments(
                word = word,
                offset = offset
            )
        }.mapSuccess { it.items }
    }

    override suspend fun fetchSearchedUsers(word: String, offset: Int?): CallResult<List<User>> {
        return safeApiCall {
            api.fetchSearchedUsers(
                word = word,
                offset = offset
            )
        }.mapSuccess { it.items }
    }
}