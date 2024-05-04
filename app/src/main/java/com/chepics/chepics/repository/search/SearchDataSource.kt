package com.chepics.chepics.repository.search

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult

interface SearchDataSource {
    suspend fun fetchSearchedTopics(word: String): CallResult<List<Topic>>
    suspend fun fetchSearchedComments(word: String): CallResult<List<Comment>>
    suspend fun fetchSearchedUsers(word: String): CallResult<List<User>>
}