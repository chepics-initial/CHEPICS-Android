package com.chepics.chepics.usecase

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.comment.CommentRepository
import com.chepics.chepics.repository.topic.TopicRepository
import com.chepics.chepics.repository.user.UserRepository
import javax.inject.Inject

interface ProfileUseCase {
    suspend fun fetchUser(userId: String): CallResult<User>
    suspend fun fetchTopics(userId: String, offset: Int?): CallResult<List<Topic>>
    suspend fun fetchComments(userId: String, offset: Int?): CallResult<List<Comment>>
}

internal class ProfileUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val topicRepository: TopicRepository,
    private val commentRepository: CommentRepository
): ProfileUseCase {
    override suspend fun fetchUser(userId: String): CallResult<User> {
        return userRepository.fetchUser(userId)
    }

    override suspend fun fetchTopics(userId: String, offset: Int?): CallResult<List<Topic>> {
        return topicRepository.fetchUserTopics(userId = userId, offset = offset)
    }

    override suspend fun fetchComments(userId: String, offset: Int?): CallResult<List<Comment>> {
        return commentRepository.fetchUserComments(userId = userId, offset = offset)
    }
}