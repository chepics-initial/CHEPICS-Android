package com.chepics.chepics.usecase

import android.net.Uri
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.topic.TopicRepository
import javax.inject.Inject

interface CreateTopicUseCase {
    suspend fun createTopic(
        title: String,
        link: String?,
        description: String?,
        images: List<Uri>?
    ): CallResult<Unit>
}

internal class CreateTopicUseCaseImpl @Inject constructor(
    private val topicRepository: TopicRepository
) : CreateTopicUseCase {
    override suspend fun createTopic(
        title: String,
        link: String?,
        description: String?,
        images: List<Uri>?
    ): CallResult<Unit> {
        return topicRepository.createTopic(
            title = title,
            link = link,
            description = description,
            images = images
        )
    }
}