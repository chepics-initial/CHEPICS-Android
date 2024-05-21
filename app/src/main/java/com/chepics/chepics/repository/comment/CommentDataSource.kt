package com.chepics.chepics.repository.comment

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.common.CallResult

interface CommentDataSource {
    suspend fun fetchFollowingComments(offset: Int?): CallResult<List<Comment>>
    suspend fun fetchUserComments(userId: String, offset: Int?): CallResult<List<Comment>>
}