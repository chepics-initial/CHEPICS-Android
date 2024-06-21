package com.chepics.chepics.feature.explore.result

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.APIErrorCode
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.InfraException
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.usecase.ExploreResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreResultViewModel @Inject constructor(private val exploreResultUseCase: ExploreResultUseCase) :
    ViewModel() {
    val searchText: MutableState<String> = mutableStateOf("")
    val selectedTab: MutableState<Int> = mutableIntStateOf(0)
    val topicUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val commentUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val userUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val topics: MutableState<List<Topic>> = mutableStateOf(emptyList())
    val comments: MutableState<ImmutableList<Comment>?> = mutableStateOf(null)
    val users: MutableState<List<User>> = mutableStateOf(emptyList())
    val topicScrollState: MutableState<LazyListState> = mutableStateOf(LazyListState())
    val commentScrollState: MutableState<LazyListState> = mutableStateOf(LazyListState())
    val userScrollState: MutableState<LazyListState> = mutableStateOf(LazyListState())
    val selectedImageIndex: MutableState<Int?> = mutableStateOf(null)
    val searchImages: MutableState<List<String>?> = mutableStateOf(null)
    val showLikeCommentFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    val showLikeReplyFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    var initialSearchText: String = ""

    init {
        viewModelScope.launch {
            fetchTopics()
        }
    }

    fun onStart(searchText: String) {
        initialSearchText = searchText
        this.searchText.value = searchText
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

            2 -> {
                if (userUIState.value != UIState.SUCCESS) {
                    viewModelScope.launch {
                        fetchUsers()
                    }
                }
            }
        }
    }

    suspend fun fetchTopics() {
        if (topicUIState.value != UIState.SUCCESS) {
            topicUIState.value = UIState.LOADING
        }
        when (val result = exploreResultUseCase.fetchTopics(initialSearchText)) {
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
        when (val result = exploreResultUseCase.fetchComments(initialSearchText)) {
            is CallResult.Success -> {
                comments.value = result.data.toImmutableList()
                commentUIState.value = UIState.SUCCESS
            }

            is CallResult.Error -> {
                commentUIState.value = UIState.FAILURE
            }
        }
    }

    suspend fun fetchUsers() {
        if (userUIState.value != UIState.SUCCESS) {
            userUIState.value = UIState.LOADING
        }

        when (val result = exploreResultUseCase.fetchUsers(initialSearchText)) {
            is CallResult.Success -> {
                users.value = result.data
                userUIState.value = UIState.SUCCESS
            }

            is CallResult.Error -> {
                userUIState.value = UIState.FAILURE
            }
        }
    }

    fun onTapImage(index: Int, images: List<String>) {
        selectedImageIndex.value = index
        searchImages.value = images
    }

    fun onTapLikeButton(comment: Comment) {
        viewModelScope.launch {
            when (val result =
                exploreResultUseCase.like(setId = comment.setId, commentId = comment.id)) {
                is CallResult.Success -> {
                    comments.value = comments.value?.map {
                        if (it.id == result.data.commentId) it.copy(
                            votes = result.data.count,
                            isLiked = result.data.isLiked
                        ) else it
                    }?.toImmutableList()
                }

                is CallResult.Error -> {
                    if (result.exception is InfraException.Server) {
                        if (result.exception.errorCode == APIErrorCode.ERROR_SET_NOT_PICKED) {
                            showLikeCommentFailureDialog.value = true
                            return@launch
                        }
                        if (result.exception.errorCode == APIErrorCode.ERROR_TOPIC_NOT_PICKED) {
                            showLikeReplyFailureDialog.value = true
                            return@launch
                        }
                    }
                }
            }
        }
    }
}

data class SearchTabItem(
    val title: String
)

val searchTabItems = listOf(
    SearchTabItem(title = "トピック"),
    SearchTabItem(title = "コメント"),
    SearchTabItem(title = "ユーザー")
)