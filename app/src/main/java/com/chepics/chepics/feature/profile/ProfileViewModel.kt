package com.chepics.chepics.feature.profile

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.mock.mockComment1
import com.chepics.chepics.mock.mockComment2
import com.chepics.chepics.mock.mockComment3
import com.chepics.chepics.mock.mockComment4
import com.chepics.chepics.mock.mockComment5
import com.chepics.chepics.mock.mockComment6
import com.chepics.chepics.mock.mockTopic1
import com.chepics.chepics.mock.mockTopic10
import com.chepics.chepics.mock.mockTopic11
import com.chepics.chepics.mock.mockTopic12
import com.chepics.chepics.mock.mockTopic13
import com.chepics.chepics.mock.mockTopic14
import com.chepics.chepics.mock.mockTopic15
import com.chepics.chepics.mock.mockTopic2
import com.chepics.chepics.mock.mockTopic3
import com.chepics.chepics.mock.mockTopic4
import com.chepics.chepics.mock.mockTopic5
import com.chepics.chepics.mock.mockTopic6
import com.chepics.chepics.mock.mockTopic7
import com.chepics.chepics.mock.mockTopic8
import com.chepics.chepics.mock.mockTopic9
import com.chepics.chepics.mock.mockUser1
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(): ViewModel() {
    val topicUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val commentUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val selectedTab: MutableState<Int> = mutableIntStateOf(0)
    val selectedImageIndex: MutableState<Int?> = mutableStateOf(null)
    val topics: MutableState<List<Topic>> = mutableStateOf(emptyList())
    val comments: MutableState<List<Comment>> = mutableStateOf(emptyList())
    val topicScrollState: MutableState<LazyListState> = mutableStateOf(LazyListState())
    val commentScrollState: MutableState<LazyListState> = mutableStateOf(LazyListState())
    val profileImages: MutableState<List<String>?> = mutableStateOf(null)
    val user: MutableState<User?> = mutableStateOf(null)

    fun onAppear(userId: String) {
        user.value = fetchUser(userId)
        viewModelScope.launch {
            fetchTopics()
        }
    }

    private fun fetchUser(userId: String): User {
        return mockUser1
    }
    private suspend fun fetchTopics() {
        if (topicUIState.value != UIState.SUCCESS) {
            topicUIState.value = UIState.LOADING
        }
        delay(1000L)
        topics.value = listOf(
            mockTopic1,
            mockTopic2,
            mockTopic3,
            mockTopic4,
            mockTopic5,
            mockTopic6,
            mockTopic7,
            mockTopic8,
            mockTopic9,
            mockTopic10,
            mockTopic11,
            mockTopic12,
            mockTopic13,
            mockTopic14,
            mockTopic15
        )
        topicUIState.value = UIState.SUCCESS
    }

    private suspend fun fetchComments() {
        if (commentUIState.value != UIState.SUCCESS) {
            commentUIState.value = UIState.LOADING
        }
        delay(1000L)
        comments.value = listOf(
            mockComment1,
            mockComment2,
            mockComment3,
            mockComment4,
            mockComment5,
            mockComment6
        )
        commentUIState.value = UIState.SUCCESS
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
        profileImages.value = images
    }
}

data class ProfileTabItem(
    val title: String
)

val profileTabItems = listOf(ProfileTabItem(title = "トピック"), ProfileTabItem(title = "コメント"))