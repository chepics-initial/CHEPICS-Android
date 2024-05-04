package com.chepics.chepics.infra.datasource.api.search

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.mock.mockComment1
import com.chepics.chepics.mock.mockComment2
import com.chepics.chepics.mock.mockComment3
import com.chepics.chepics.mock.mockComment4
import com.chepics.chepics.mock.mockComment5
import com.chepics.chepics.mock.mockComment6
import com.chepics.chepics.mock.mockTopic1
import com.chepics.chepics.mock.mockTopic10
import com.chepics.chepics.mock.mockTopic11
import com.chepics.chepics.mock.mockTopic12
import com.chepics.chepics.mock.mockTopic13
import com.chepics.chepics.mock.mockTopic14
import com.chepics.chepics.mock.mockTopic15
import com.chepics.chepics.mock.mockTopic2
import com.chepics.chepics.mock.mockTopic3
import com.chepics.chepics.mock.mockTopic4
import com.chepics.chepics.mock.mockTopic5
import com.chepics.chepics.mock.mockTopic6
import com.chepics.chepics.mock.mockTopic7
import com.chepics.chepics.mock.mockTopic8
import com.chepics.chepics.mock.mockTopic9
import com.chepics.chepics.mock.mockUser1
import com.chepics.chepics.mock.mockUser10
import com.chepics.chepics.mock.mockUser2
import com.chepics.chepics.mock.mockUser3
import com.chepics.chepics.mock.mockUser4
import com.chepics.chepics.mock.mockUser5
import com.chepics.chepics.mock.mockUser6
import com.chepics.chepics.mock.mockUser7
import com.chepics.chepics.mock.mockUser8
import com.chepics.chepics.mock.mockUser9
import com.chepics.chepics.repository.search.SearchDataSource
import javax.inject.Inject

class SearchRemoteSource @Inject constructor(private val api: SearchApi): SearchDataSource {
    override suspend fun fetchSearchedTopics(word: String): CallResult<List<Topic>> {
        return CallResult.Success(data = listOf(
            mockTopic1,
            mockTopic2,
            mockTopic3,
            mockTopic4,
            mockTopic5,
            mockTopic6,
            mockTopic7,
            mockTopic8,
            mockTopic9,
            mockTopic10,
            mockTopic11,
            mockTopic12,
            mockTopic13,
            mockTopic14,
            mockTopic15
        ))
    }

    override suspend fun fetchSearchedComments(word: String): CallResult<List<Comment>> {
        return CallResult.Success(data = listOf(
            mockComment6,
            mockComment5,
            mockComment4,
            mockComment3,
            mockComment2,
            mockComment1
        ))
    }

    override suspend fun fetchSearchedUsers(word: String): CallResult<List<User>> {
        return CallResult.Success(data = listOf(
            mockUser1,
            mockUser2,
            mockUser3,
            mockUser4,
            mockUser5,
            mockUser6,
            mockUser7,
            mockUser8,
            mockUser9,
            mockUser10
        ))
    }
}