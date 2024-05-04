package com.chepics.chepics.feature.feed

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.usecase.feed.FeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val feedUseCase: FeedUseCase) : ViewModel() {
    val topicUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val commentUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val topics: MutableState<List<Topic>> = mutableStateOf(emptyList())
    val comments: MutableState<List<Comment>> = mutableStateOf(emptyList())
    val selectedTab: MutableState<Int> = mutableIntStateOf(0)
    val selectedImageIndex: MutableState<Int?> = mutableStateOf(null)
    val feedImages: MutableState<List<String>?> = mutableStateOf(null)
    val topicScrollState: MutableState<LazyListState> = mutableStateOf(LazyListState())
    val commentScrollState: MutableState<LazyListState> = mutableStateOf(LazyListState())

    init {
        viewModelScope.launch {
            fetchTopics()
        }
    }

    suspend fun fetchTopics() {
        if (topicUIState.value != UIState.SUCCESS) {
            topicUIState.value = UIState.LOADING
        }
        when (val result = feedUseCase.fetchFavoriteTopics(null)) {
            is CallResult.Success -> {
                topics.value = result.data
                topicUIState.value = UIState.SUCCESS
            }

            is CallResult.Error -> {
                topicUIState.value = UIState.FAILURE
            }
        }
    }

    suspend fun fetchComments() {
        if (commentUIState.value != UIState.SUCCESS) {
            commentUIState.value = UIState.LOADING
        }
        when (val result = feedUseCase.fetchComments(null)) {
            is CallResult.Success -> {
                comments.value = result.data
                commentUIState.value = UIState.SUCCESS
            }

            is CallResult.Error -> {
                commentUIState.value = UIState.FAILURE
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
                if (commentUIState.value != UIState.SUCCESS) {
                    viewModelScope.launch {
                        fetchComments()
                    }
                }
            }
        }
    }

    fun onTapImage(index: Int, images: List<String>) {
        selectedImageIndex.value = index
        feedImages.value = images
    }
}

data class FeedTabItem(
    val title: String
)

val feedTabItems = listOf(FeedTabItem(title = "おすすめ"), FeedTabItem(title = "フォロー中"))