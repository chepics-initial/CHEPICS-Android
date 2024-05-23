package com.chepics.chepics.usecase

import com.chepics.chepics.domainmodel.CreateSetRequest
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.set.SetRepository
import javax.inject.Inject

interface CreateSetUseCase {
    suspend fun createSet(topicId: String, set: String): CallResult<Unit>
}

internal class CreateSetUseCaseImpl @Inject constructor(
    private val setRepository: SetRepository
) : CreateSetUseCase {
    override suspend fun createSet(topicId: String, set: String): CallResult<Unit> {
        return setRepository.createSet(CreateSetRequest(topicId = topicId, setText = set))
    }
}