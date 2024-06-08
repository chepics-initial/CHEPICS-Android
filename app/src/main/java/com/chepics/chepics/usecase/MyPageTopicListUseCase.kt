package com.chepics.chepics.usecase

import com.chepics.chepics.domainmodel.MySet
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.set.SetRepository
import javax.inject.Inject

interface MyPageTopicListUseCase {
    suspend fun fetchPickedSets(offset: Int?): CallResult<List<MySet>>
}

internal class MyPageTopicListUseCaseImpl @Inject constructor(
    private val setRepository: SetRepository
) : MyPageTopicListUseCase {
    override suspend fun fetchPickedSets(offset: Int?): CallResult<List<MySet>> {
        return setRepository.fetchPickedSets(offset)
    }
}