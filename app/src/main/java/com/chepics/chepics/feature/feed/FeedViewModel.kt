package com.chepics.chepics.feature.feed

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.usecase.feed.FeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val feedUseCase: FeedUseCase): ViewModel() {
    val topicUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val commentUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val topics: MutableState<List<Topic>> = mutableStateOf(emptyList())
    val selectedTab: MutableState<FeedTabType> = mutableStateOf(FeedTabType.TOPICS)
    init {
        viewModelScope.launch {
            fetchTopics()
        }
    }

    suspend fun fetchTopics() {
        if (topicUIState.value != UIState.SUCCESS) {
            topicUIState.value = UIState.LOADING
        }
        when (val result = feedUseCase.fetchFavoriteTopics()) {
            is CallResult.Success -> {
                topics.value = result.data
                topicUIState.value = UIState.SUCCESS
            }
            is CallResult.Error -> {
                topicUIState.value = UIState.FAILURE
            }
        }
    }

    fun selectTab(type: FeedTabType) {
        selectedTab.value = type
        when (type) {
            FeedTabType.TOPICS -> {
                if (topicUIState.value != UIState.SUCCESS) {
                    viewModelScope.launch {
                        fetchTopics()
                    }
                }
            }
            FeedTabType.COMMENTS -> {
                Log.d("Comment", "selectTab: Comment selected")
            }
        }
    }
}

enum class UIState {
    LOADING,
    SUCCESS,
    FAILURE
}

enum class FeedTabType {
    TOPICS,
    COMMENTS;

    fun getTitle(): String {
        return when (this) {
            TOPICS -> "おすすめ"
            COMMENTS -> "フォロー中"
        }
    }
}