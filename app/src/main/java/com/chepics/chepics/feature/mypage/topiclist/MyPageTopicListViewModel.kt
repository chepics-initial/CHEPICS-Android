package com.chepics.chepics.feature.mypage.topiclist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.MySet
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.usecase.MyPageTopicListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageTopicListViewModel @Inject constructor(private val myPageTopicListUseCase: MyPageTopicListUseCase) :
    ViewModel() {
    val uiState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val sets: MutableState<List<MySet>> = mutableStateOf(emptyList())

    init {
        viewModelScope.launch {
            fetchSets()
        }
    }

    suspend fun fetchSets() {
        when (val result = myPageTopicListUseCase.fetchPickedSets(null)) {
            is CallResult.Success -> {
                sets.value = result.data
                uiState.value = UIState.SUCCESS
            }

            is CallResult.Error -> uiState.value = UIState.FAILURE
        }
    }
}