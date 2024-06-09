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
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.usecase.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileUseCase: ProfileUseCase) :
    ViewModel() {
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
    val isFollowing: MutableState<Boolean?> = mutableStateOf(null)
    val isCurrentUser: MutableState<Boolean> = mutableStateOf(false)
    val isEnabled: MutableState<Boolean> = mutableStateOf(true)
    val showDialog: MutableState<Boolean> = mutableStateOf(false)
    private var isInitialOnStart = true

    fun onStart(user: User) {
        this.user.value = user
        isCurrentUser.value = user.id == profileUseCase.getCurrentUserId()
        isFollowing.value = user.isFollowing
        if (isInitialOnStart) {
            isInitialOnStart = false
            viewModelScope.launch {
                fetchUser(user.id)
                fetchTopics()
            }
        }
    }

    private suspend fun fetchUser(userId: String) {
        when (val result = profileUseCase.fetchUser(userId)) {
            is CallResult.Success -> {
                user.value = result.data
                isFollowing.value = result.data.isFollowing
            }

            is CallResult.Error -> return
        }
    }

    private suspend fun fetchTopics() {
        user.value?.let {
            if (topicUIState.value != UIState.SUCCESS) {
                topicUIState.value = UIState.LOADING
            }
            when (val result = profileUseCase.fetchTopics(userId = it.id, offset = null)) {
                is CallResult.Success -> {
                    topics.value = result.data
                    topicUIState.value = UIState.SUCCESS
                }

                is CallResult.Error -> topicUIState.value = UIState.FAILURE
            }
        }
    }

    private suspend fun fetchComments() {
        user.value?.let {
            if (commentUIState.value != UIState.SUCCESS) {
                commentUIState.value = UIState.LOADING
            }
            when (val result = profileUseCase.fetchComments(userId = it.id, offset = null)) {
                is CallResult.Success -> {
                    comments.value = result.data
                    commentUIState.value = UIState.SUCCESS
                }

                is CallResult.Error -> commentUIState.value = UIState.FAILURE
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
        profileImages.value = images
    }

    fun onTapFollowButton() {
        user.value?.let { forUser ->
            viewModelScope.launch {
                isEnabled.value = false
                when (val result = profileUseCase.follow(forUser.id)) {
                    is CallResult.Success -> {
                        isEnabled.value = true
                        isFollowing.value = result.data
                    }

                    is CallResult.Error -> showDialog.value = true
                }
            }
        }
    }
}

data class ProfileTabItem(
    val title: String
)

val profileTabItems = listOf(ProfileTabItem(title = "トピック"), ProfileTabItem(title = "コメント"))