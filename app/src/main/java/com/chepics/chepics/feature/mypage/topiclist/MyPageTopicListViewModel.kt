package com.chepics.chepics.feature.mypage.topiclist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.MySet
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.feature.common.FooterStatus
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.usecase.MyPageTopicListUseCase
import com.chepics.chepics.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageTopicListViewModel @Inject constructor(private val myPageTopicListUseCase: MyPageTopicListUseCase) :
    ViewModel() {
    val uiState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val sets: MutableState<List<MySet>> = mutableStateOf(emptyList())
    val footerStatus: MutableState<FooterStatus> = mutableStateOf(FooterStatus.LOADINGSTOPPED)
    val isRefreshing: MutableState<Boolean> = mutableStateOf(false)
    private var offset = 0

    init {
        viewModelScope.launch {
            fetchSets()
        }
    }

    private suspend fun fetchSets() {
        when (val result = myPageTopicListUseCase.fetchPickedSets(null)) {
            is CallResult.Success -> {
                sets.value = result.data
                if (result.data.size < Constants.ARRAY_LIMIT) {
                    footerStatus.value = FooterStatus.ALLFETCHED
                } else {
                    footerStatus.value = FooterStatus.LOADINGSTOPPED
                }
                uiState.value = UIState.SUCCESS
                offset = Constants.ARRAY_LIMIT
            }

            is CallResult.Error -> uiState.value = UIState.FAILURE
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            isRefreshing.value = true
            fetchSets()
            isRefreshing.value = false
        }
    }

    fun onReachFooterView() {
        if (footerStatus.value == FooterStatus.LOADINGSTOPPED || footerStatus.value == FooterStatus.FAILURE) {
            footerStatus.value = FooterStatus.LOADINGSTARTED
            viewModelScope.launch {
                when (val result = myPageTopicListUseCase.fetchPickedSets(offset)) {
                    is CallResult.Success -> {
                        val updatedSets = sets.value.toMutableList()
                        for (additionalSet in result.data) {
                            val index =
                                sets.value.indexOfFirst { it.set.id == additionalSet.set.id }
                            if (index != -1) {
                                updatedSets[index] = additionalSet
                            } else {
                                updatedSets.add(additionalSet)
                            }
                        }
                        sets.value = updatedSets
                        if (result.data.size < Constants.ARRAY_LIMIT) {
                            footerStatus.value = FooterStatus.ALLFETCHED
                            offset = 0
                            return@launch
                        }
                        footerStatus.value = FooterStatus.LOADINGSTOPPED
                        offset += Constants.ARRAY_LIMIT
                    }

                    is CallResult.Error -> footerStatus.value = FooterStatus.FAILURE
                }
            }
        }
    }
}