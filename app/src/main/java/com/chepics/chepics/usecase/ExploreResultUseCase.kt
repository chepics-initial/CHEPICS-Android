package com.chepics.chepics.usecase

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.LikeRequest
import com.chepics.chepics.domainmodel.LikeResponse
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.comment.CommentRepository
import com.chepics.chepics.repository.search.SearchRepository
import javax.inject.Inject

interface ExploreResultUseCase {
    suspend fun fetchTopics(word: String, offset: Int?): CallResult<List<Topic>>
    suspend fun fetchComments(word: String, offset: Int?): CallResult<List<Comment>>
    suspend fun fetchUsers(word: String, offset: Int?): CallResult<List<User>>
    suspend fun like(setId: String, commentId: String): CallResult<LikeResponse>
}

internal class ExploreResultUseCaseImpl @Inject constructor(
    private val searchRepository: SearchRepository,
    private val commentRepository: CommentRepository
) : ExploreResultUseCase {
    override suspend fun fetchTopics(word: String, offset: Int?): CallResult<List<Topic>> {
        return searchRepository.fetchSearchedTopics(word = word, offset = offset)
    }

    override suspend fun fetchComments(word: String, offset: Int?): CallResult<List<Comment>> {
        return searchRepository.fetchSearchedComments(word = word, offset = offset)
    }

    override suspend fun fetchUsers(word: String, offset: Int?): CallResult<List<User>> {
        return searchRepository.fetchSearchedUsers(word = word, offset = offset)
    }

    override suspend fun like(setId: String, commentId: String): CallResult<LikeResponse> {
        return commentRepository.like(LikeRequest(setId = setId, commentId = commentId))
    }
}