package com.chepics.chepics.feature.topic.top

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.APIErrorCode
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.InfraException
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.feature.common.FooterStatus
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.infra.datasource.api.EMPTY_RESPONSE
import com.chepics.chepics.usecase.TopicTopUseCase
import com.chepics.chepics.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopicTopViewModel @Inject constructor(private val topicTopUseCase: TopicTopUseCase) :
    ViewModel() {
    val topic: MutableState<Topic?> = mutableStateOf(null)
    val selectedImageIndex: MutableState<Int?> = mutableStateOf(null)
    val listImages: MutableState<List<String>?> = mutableStateOf(null)
    val setListUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val commentUIState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val sets: MutableState<List<PickSet>> = mutableStateOf(emptyList())
    val comments: MutableState<ImmutableList<Comment>?> = mutableStateOf(null)
    val selectedSet: MutableState<PickSet?> = mutableStateOf(null)
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val status: MutableState<TopicTopStatus> = mutableStateOf(TopicTopStatus.LOADING)
    val showBottomSheet: MutableState<Boolean> = mutableStateOf(false)
    val showAlert: MutableState<Boolean> = mutableStateOf(false)
    val currentSet: MutableState<PickSet?> = mutableStateOf(null)
    val showLikeCommentFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    val showLikeReplyFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    val commentFooterStatus: MutableState<FooterStatus> =
        mutableStateOf(FooterStatus.LOADINGSTOPPED)
    val setFooterStatus: MutableState<FooterStatus> = mutableStateOf(FooterStatus.LOADINGSTOPPED)
    var topicId: String? = null
    private var isInitialOnStart = true
    private var setOffset = 0
    private var commentOffset = 0

    fun onStart(topicId: String, rootTopic: Topic?) {
        this.topicId = topicId
        if (isInitialOnStart || status.value == TopicTopStatus.FAILURE) {
            isInitialOnStart = false
            this.topic.value = rootTopic
            viewModelScope.launch {
                when (val result = topicTopUseCase.fetchPickedSet(topicId)) {
                    is CallResult.Success -> {
                        fetchTopic()
                        if (topic.value != null) {
                            status.value = TopicTopStatus.DETAIL
                            selectedSet.value = result.data
                            currentSet.value = result.data
                            fetchComments(setId = result.data.id)
                            return@launch
                        }
                        status.value = TopicTopStatus.FAILURE
                    }

                    is CallResult.Error -> {
                        if (result.exception.message == EMPTY_RESPONSE) {
                            fetchTopic()
                            if (topic.value != null) {
                                status.value = TopicTopStatus.TOP
                                return@launch
                            }
                        }

                        status.value = TopicTopStatus.FAILURE
                    }
                }
            }
        }
    }

    fun onTapLikeButton(comment: Comment) {
        viewModelScope.launch {
            when (val result =
                topicTopUseCase.like(setId = comment.setId, commentId = comment.id)) {
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

    private suspend fun fetchTopic() {
        topicId?.let {
            when (val result = topicTopUseCase.fetchTopic(it)) {
                is CallResult.Success -> topic.value = result.data
                is CallResult.Error -> return
            }
        }
    }

    fun onTapImage(index: Int, images: List<String>) {
        selectedImageIndex.value = index
        listImages.value = images
    }

    fun fetchSets() {
        topic.value?.let {
            viewModelScope.launch {
                when (val result = topicTopUseCase.fetchSets(topicId = it.id, offset = null)) {
                    is CallResult.Success -> {
                        sets.value = result.data
                        if (result.data.size < Constants.ARRAY_LIMIT) {
                            setFooterStatus.value = FooterStatus.ALLFETCHED
                        } else {
                            setFooterStatus.value = FooterStatus.LOADINGSTOPPED
                        }
                        setListUIState.value = UIState.SUCCESS
                        setOffset = Constants.ARRAY_LIMIT
                    }

                    is CallResult.Error -> setListUIState.value = UIState.FAILURE
                }
            }
        }
    }

    fun selectSet(set: PickSet) {
        selectedSet.value = set
    }

    fun onTapSelectButton() {
        selectedSet.value?.let { set ->
            topic.value?.let { topic ->
                viewModelScope.launch {
                    showBottomSheet.value = false
                    isLoading.value = true
                    when (val result =
                        topicTopUseCase.pickSet(topicId = topic.id, setId = set.id)) {
                        is CallResult.Success -> {
                            isLoading.value = false
                            fetchTopic()
                            status.value = TopicTopStatus.DETAIL
                            selectedSet.value = result.data
                            currentSet.value = result.data
                            fetchComments(setId = result.data.id)
                        }

                        is CallResult.Error -> {
                            isLoading.value = false
                            showAlert.value = true
                        }
                    }
                }
            }
        }
    }

    fun isSelectButtonActive(): Boolean {
        return selectedSet.value != null && selectedSet.value?.id != currentSet.value?.id
    }

    fun onReachCommentFooterView() {
        currentSet.value?.let {
            if (commentFooterStatus.value == FooterStatus.LOADINGSTOPPED || commentFooterStatus.value == FooterStatus.FAILURE) {
                commentFooterStatus.value = FooterStatus.LOADINGSTARTED
                viewModelScope.launch {
                    when (val result = topicTopUseCase.fetchSetComments(
                        setId = it.id,
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
    }

    fun onReachSetFooterView() {
        topic.value?.let {
            if (setFooterStatus.value == FooterStatus.LOADINGSTOPPED || setFooterStatus.value == FooterStatus.FAILURE) {
                setFooterStatus.value = FooterStatus.LOADINGSTARTED
                viewModelScope.launch {
                    when (val result =
                        topicTopUseCase.fetchSets(topicId = it.id, offset = setOffset)) {
                        is CallResult.Success -> {
                            val updatedSets = sets.value.toMutableList()
                            for (additionalSet in result.data) {
                                val index = sets.value.indexOfFirst { it.id == additionalSet.id }
                                if (index != -1) {
                                    updatedSets[index] = additionalSet
                                } else {
                                    updatedSets.add(additionalSet)
                                }
                            }
                            sets.value = updatedSets
                            if (result.data.size < Constants.ARRAY_LIMIT) {
                                setFooterStatus.value = FooterStatus.ALLFETCHED
                                setOffset = 0
                                return@launch
                            }
                            setFooterStatus.value = FooterStatus.LOADINGSTOPPED
                            setOffset += Constants.ARRAY_LIMIT
                        }

                        is CallResult.Error -> setFooterStatus.value = FooterStatus.FAILURE
                    }
                }
            }
        }
    }

    fun createCommentCompletion() {
        topicId?.let {
            viewModelScope.launch {
                when (val result = topicTopUseCase.fetchPickedSet(it)) {
                    is CallResult.Success -> {
                        selectedSet.value = result.data
                        currentSet.value = result.data
                        fetchComments(setId = result.data.id)
                    }

                    is CallResult.Error -> return@launch
                }
            }
        }
    }

    private suspend fun fetchComments(setId: String) {
        when (val result = topicTopUseCase.fetchSetComments(setId = setId, offset = null)) {
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
                if (comments.value == null) {
                    commentUIState.value = UIState.FAILURE
                }
            }
        }
    }
}

enum class TopicTopStatus {
    TOP,
    DETAIL,
    LOADING,
    FAILURE
}