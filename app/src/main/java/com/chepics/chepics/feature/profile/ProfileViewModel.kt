package com.chepics.chepics.feature.profile

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
import com.chepics.chepics.feature.common.FooterStatus
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.usecase.ProfileUseCase
import com.chepics.chepics.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
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
    val comments: MutableState<ImmutableList<Comment>?> = mutableStateOf(null)
    val topicScrollState: MutableState<LazyListState> = mutableStateOf(LazyListState())
    val commentScrollState: MutableState<LazyListState> = mutableStateOf(LazyListState())
    val profileImages: MutableState<List<String>?> = mutableStateOf(null)
    val user: MutableState<User?> = mutableStateOf(null)
    val isFollowing: MutableState<Boolean?> = mutableStateOf(null)
    val isCurrentUser: MutableState<Boolean> = mutableStateOf(false)
    val isEnabled: MutableState<Boolean> = mutableStateOf(true)
    val showDialog: MutableState<Boolean> = mutableStateOf(false)
    val showLikeCommentFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    val showLikeReplyFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    val topicFooterStatus: MutableState<FooterStatus> = mutableStateOf(FooterStatus.LOADINGSTOPPED)
    val commentFooterStatus: MutableState<FooterStatus> =
        mutableStateOf(FooterStatus.LOADINGSTOPPED)
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
                    if (result.data.size < Constants.ARRAY_LIMIT) {
                        topicFooterStatus.value = FooterStatus.ALLFETCHED
                    } else {
                        topicFooterStatus.value = FooterStatus.LOADINGSTOPPED
                    }
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
                    comments.value = result.data.toImmutableList()
                    if (result.data.size < Constants.ARRAY_LIMIT) {
                        commentFooterStatus.value = FooterStatus.ALLFETCHED
                    } else {
                        commentFooterStatus.value = FooterStatus.LOADINGSTOPPED
                    }
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

    fun onTapLikeButton(comment: Comment) {
        viewModelScope.launch {
            when (val result = profileUseCase.like(setId = comment.setId, commentId = comment.id)) {
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

    fun onReachTopicFooterView() {
        user.value?.let {
            if (topicFooterStatus.value == FooterStatus.LOADINGSTOPPED || topicFooterStatus.value == FooterStatus.FAILURE) {
                topicFooterStatus.value = FooterStatus.LOADINGSTARTED
                viewModelScope.launch {
                    when (val result =
                        profileUseCase.fetchTopics(userId = it.id, offset = topics.value.size)) {
                        is CallResult.Success -> {
                            val updatedTopics = topics.value.toMutableList()
                            for (additionalTopic in result.data) {
                                val index =
                                    topics.value.indexOfFirst { it.id == additionalTopic.id }
                                if (index != -1) {
                                    updatedTopics[index] = additionalTopic
                                } else {
                                    updatedTopics.add(additionalTopic)
                                }
                            }
                            topics.value = updatedTopics
                            if (result.data.size < Constants.ARRAY_LIMIT) {
                                topicFooterStatus.value = FooterStatus.ALLFETCHED
                            } else {
                                topicFooterStatus.value = FooterStatus.LOADINGSTOPPED
                            }
                        }

                        is CallResult.Error -> topicFooterStatus.value = FooterStatus.FAILURE
                    }
                }
            }
        }
    }

    fun onReachCommentFooterView() {
        user.value?.let {
            if (commentFooterStatus.value == FooterStatus.LOADINGSTOPPED || commentFooterStatus.value == FooterStatus.FAILURE) {
                commentFooterStatus.value = FooterStatus.LOADINGSTARTED
                viewModelScope.launch {
                    when (val result = profileUseCase.fetchComments(
                        userId = it.id,
                        offset = comments.value?.size
                    )) {
                        is CallResult.Success -> {
                            val updatedComments = comments.value?.toMutableList()
                            for (additionalComment in result.data) {
                                val index =
                                    comments.value?.indexOfFirst { it.id == additionalComment.id }
                                if (index != null && index != -1) {
                                    updatedComments?.set(index, additionalComment)
                                } else {
                                    updatedComments?.add(additionalComment)
                                }
                            }
                            comments.value = updatedComments?.toImmutableList()
                            if (result.data.size < Constants.ARRAY_LIMIT) {
                                commentFooterStatus.value = FooterStatus.ALLFETCHED
                            } else {
                                commentFooterStatus.value = FooterStatus.LOADINGSTOPPED
                            }
                        }

                        is CallResult.Error -> commentFooterStatus.value = FooterStatus.FAILURE
                    }
                }
            }
        }
    }
}

data class ProfileTabItem(
    val title: String
)

val profileTabItems = listOf(ProfileTabItem(title = "トピック"), ProfileTabItem(title = "コメント"))