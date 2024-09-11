package com.chepics.chepics.feature.feed

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
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.feature.common.FooterStatus
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.usecase.feed.FeedUseCase
import com.chepics.chepics.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(private val feedUseCase: FeedUseCase) : ViewModel() {
    val topicUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val commentUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val topics: MutableState<List<Topic>> = mutableStateOf(emptyList())
    val comments: MutableState<ImmutableList<Comment>?> = mutableStateOf(null)
    val selectedTab: MutableState<Int> = mutableIntStateOf(0)
    val selectedImageIndex: MutableState<Int?> = mutableStateOf(null)
    val feedImages: MutableState<List<String>?> = mutableStateOf(null)
    val topicScrollState: MutableState<LazyListState> = mutableStateOf(LazyListState())
    val commentScrollState: MutableState<LazyListState> = mutableStateOf(LazyListState())
    val showLikeCommentFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    val showLikeReplyFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    val topicFooterStatus: MutableState<FooterStatus> = mutableStateOf(FooterStatus.LOADINGSTOPPED)
    val commentFooterStatus: MutableState<FooterStatus> =
        mutableStateOf(FooterStatus.LOADINGSTOPPED)
    private var isTopicFetchStarted = false
    private var isCommentFetchStarted = false
    private var topicOffset = 0
    private var commentOffset = 0

    init {
        viewModelScope.launch {
            fetchTopics()
        }
    }

    suspend fun fetchTopics() {
        isTopicFetchStarted = true
        when (val result = feedUseCase.fetchFavoriteTopics(null)) {
            is CallResult.Success -> {
                topics.value = result.data
                if (result.data.size < Constants.ARRAY_LIMIT) {
                    topicFooterStatus.value = FooterStatus.ALLFETCHED
                } else {
                    topicFooterStatus.value = FooterStatus.LOADINGSTOPPED
                }
                topicUIState.value = UIState.SUCCESS
                topicOffset = Constants.ARRAY_LIMIT
            }

            is CallResult.Error -> {
                topicUIState.value = UIState.FAILURE
            }
        }
    }

    suspend fun fetchComments() {
        isCommentFetchStarted = true
        when (val result = feedUseCase.fetchComments(null)) {
            is CallResult.Success -> {
                comments.value = result.data.toImmutableList()
                if (result.data.size < Constants.ARRAY_LIMIT) {
                    commentFooterStatus.value = FooterStatus.ALLFETCHED
                } else {
                    commentFooterStatus.value = FooterStatus.LOADINGSTOPPED
                }
                commentUIState.value = UIState.SUCCESS
                commentOffset = Constants.ARRAY_LIMIT
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
                if (!isTopicFetchStarted) {
                    viewModelScope.launch {
                        fetchTopics()
                    }
                }
            }

            1 -> {
                if (!isCommentFetchStarted) {
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

    fun onTapLikeButton(comment: Comment) {
        viewModelScope.launch {
            when (val result = feedUseCase.like(setId = comment.setId, commentId = comment.id)) {
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
        if (topicFooterStatus.value == FooterStatus.LOADINGSTOPPED || topicFooterStatus.value == FooterStatus.FAILURE) {
            topicFooterStatus.value = FooterStatus.LOADINGSTARTED
            viewModelScope.launch {
                when (val result = feedUseCase.fetchFavoriteTopics(topicOffset)) {
                    is CallResult.Success -> {
                        val updatedTopics = topics.value.toMutableList()
                        for (additionalTopic in result.data) {
                            val index = topics.value.indexOfFirst { it.id == additionalTopic.id }
                            if (index != -1) {
                                updatedTopics[index] = additionalTopic
                            } else {
                                updatedTopics.add(additionalTopic)
                            }
                        }
                        topics.value = updatedTopics
                        if (result.data.size < Constants.ARRAY_LIMIT) {
                            topicFooterStatus.value = FooterStatus.ALLFETCHED
                            topicOffset = 0
                            return@launch
                        }
                        topicFooterStatus.value = FooterStatus.LOADINGSTOPPED
                        topicOffset += Constants.ARRAY_LIMIT
                    }

                    is CallResult.Error -> topicFooterStatus.value = FooterStatus.FAILURE
                }
            }
        }
    }

    fun onReachCommentFooterView() {
        if (commentFooterStatus.value == FooterStatus.LOADINGSTOPPED || commentFooterStatus.value == FooterStatus.FAILURE) {
            commentFooterStatus.value = FooterStatus.LOADINGSTARTED
            viewModelScope.launch {
                when (val result = feedUseCase.fetchComments(commentOffset)) {
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
                            commentOffset = 0
                            return@launch
                        }
                        commentFooterStatus.value = FooterStatus.LOADINGSTOPPED
                        commentOffset += Constants.ARRAY_LIMIT
                    }

                    is CallResult.Error -> commentFooterStatus.value = FooterStatus.FAILURE
                }
            }
        }
    }
}

data class FeedTabItem(
    val title: String
)

val feedTabItems = listOf(FeedTabItem(title = "おすすめ"), FeedTabItem(title = "フォロー中"))