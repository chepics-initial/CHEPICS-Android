package com.chepics.chepics.repository.search

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult

interface SearchDataSource {
    suspend fun fetchSearchedTopics(word: String, offset: Int?): CallResult<List<Topic>>
    suspend fun fetchSearchedComments(word: String, offset: Int?): CallResult<List<Comment>>
    suspend fun fetchSearchedUsers(word: String, offset: Int?): CallResult<List<User>>
}