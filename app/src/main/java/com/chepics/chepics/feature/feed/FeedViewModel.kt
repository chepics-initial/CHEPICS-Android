package com.chepics.chepics.feature.feed

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
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
class FeedViewModel @Inject constructor(private val feedUseCase: FeedUseCase) : ViewModel() {
    val topicUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val commentUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val topics: MutableState<List<Topic>> = mutableStateOf(emptyList())
    val selectedTab: MutableState<Int> = mutableIntStateOf(0)
    val selectedImageIndex: MutableState<Int?> = mutableStateOf(null)
    val topicImages: MutableState<List<String>?> = mutableStateOf(null)
    val topicScrollState: MutableState<LazyListState> = mutableStateOf(LazyListState())

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

    fun selectTab(index: Int) {
        selectedTab.value = index
        when (index) {
            0 -> {
                if (topicUIState.value != UIState.SUCCESS) {
                    viewModelScope.launch {
                        fetchTopics()
                    }
                }
            }

            1 -> {
                Log.d("Comment", "selectTab: Comment selected")
            }
        }
    }

    fun onTapImage(index: Int, images: List<String>) {
        selectedImageIndex.value = index
        topicImages.value = images
    }
}

enum class UIState {
    LOADING,
    SUCCESS,
    FAILURE
}

data class FeedTabItem(
    val title: String
)

val feedTabItems = listOf(FeedTabItem(title = "おすすめ"), FeedTabItem(title = "フォロー中"))