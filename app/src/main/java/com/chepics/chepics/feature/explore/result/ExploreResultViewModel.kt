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
import com.chepics.chepics.feature.common.FooterStatus
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.usecase.ExploreResultUseCase
import com.chepics.chepics.utils.Constants
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
    val topicFooterStatus: MutableState<FooterStatus> = mutableStateOf(FooterStatus.LOADINGSTOPPED)
    val commentFooterStatus: MutableState<FooterStatus> =
        mutableStateOf(FooterStatus.LOADINGSTOPPED)
    val userFooterStatus: MutableState<FooterStatus> = mutableStateOf(FooterStatus.LOADINGSTOPPED)
    val isTopicRefreshing: MutableState<Boolean> = mutableStateOf(false)
    val isCommentRefreshing: MutableState<Boolean> = mutableStateOf(false)
    val isUserRefreshing: MutableState<Boolean> = mutableStateOf(false)
    var initialSearchText: String = ""
    private var isInitialOnStart = true
    private var topicOffset = 0
    private var commentOffset = 0
    private var userOffset = 0

    fun onStart(searchText: String) {
        initialSearchText = searchText
        this.searchText.value = searchText
        if (isInitialOnStart) {
            isInitialOnStart = false
            when (selectedTab.value) {
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

    private suspend fun fetchTopics() {
        if (topicUIState.value != UIState.SUCCESS) {
            topicUIState.value = UIState.LOADING
        }
        when (val result =
            exploreResultUseCase.fetchTopics(word = initialSearchText, offset = null)) {
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

    private suspend fun fetchComments() {
        if (commentUIState.value != UIState.SUCCESS) {
            commentUIState.value = UIState.LOADING
        }
        when (val result =
            exploreResultUseCase.fetchComments(word = initialSearchText, offset = null)) {
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

    private suspend fun fetchUsers() {
        if (userUIState.value != UIState.SUCCESS) {
            userUIState.value = UIState.LOADING
        }

        when (val result =
            exploreResultUseCase.fetchUsers(word = initialSearchText, offset = null)) {
            is CallResult.Success -> {
                users.value = result.data
                if (result.data.size < Constants.ARRAY_LIMIT) {
                    userFooterStatus.value = FooterStatus.ALLFETCHED
                } else {
                    userFooterStatus.value = FooterStatus.LOADINGSTOPPED
                }
                userUIState.value = UIState.SUCCESS
                userOffset = Constants.ARRAY_LIMIT
            }

            is CallResult.Error -> {
                userUIState.value = UIState.FAILURE
            }
        }
    }

    fun onTopicRefresh() {
        viewModelScope.launch {
            isTopicRefreshing.value = true
            fetchTopics()
            isTopicRefreshing.value = false
        }
    }

    fun onCommentRefresh() {
        viewModelScope.launch {
            isCommentRefreshing.value = true
            fetchComments()
            isCommentRefreshing.value = false
        }
    }

    fun onUserRefresh() {
        viewModelScope.launch {
            isUserRefreshing.value = true
            fetchUsers()
            isUserRefreshing.value = false
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

    fun onReachTopicFooterView() {
        if (topicFooterStatus.value == FooterStatus.LOADINGSTOPPED || topicFooterStatus.value == FooterStatus.FAILURE) {
            topicFooterStatus.value = FooterStatus.LOADINGSTARTED
            viewModelScope.launch {
                when (val result = exploreResultUseCase.fetchTopics(
                    word = initialSearchText,
                    offset = topicOffset
                )) {
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
                when (val result = exploreResultUseCase.fetchComments(
                    word = initialSearchText,
                    offset = commentOffset
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

    fun onReachUserFooterView() {
        if (userFooterStatus.value == FooterStatus.LOADINGSTOPPED || userFooterStatus.value == FooterStatus.FAILURE) {
            userFooterStatus.value = FooterStatus.LOADINGSTARTED
            viewModelScope.launch {
                when (val result = exploreResultUseCase.fetchUsers(
                    word = initialSearchText,
                    offset = userOffset
                )) {
                    is CallResult.Success -> {
                        val updatedUsers = users.value.toMutableList()
                        for (additionalUser in result.data) {
                            val index = users.value.indexOfFirst { it.id == additionalUser.id }
                            if (index != -1) {
                                updatedUsers[index] = additionalUser
                            } else {
                                updatedUsers.add(additionalUser)
                            }
                        }
                        users.value = updatedUsers
                        if (result.data.size < Constants.ARRAY_LIMIT) {
                            userFooterStatus.value = FooterStatus.ALLFETCHED
                            userOffset = 0
                            return@launch
                        }
                        userFooterStatus.value = FooterStatus.LOADINGSTOPPED
                        userOffset += Constants.ARRAY_LIMIT
                    }

                    is CallResult.Error -> userFooterStatus.value = FooterStatus.FAILURE
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