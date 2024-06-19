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
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.infra.datasource.api.EMPTY_RESPONSE
import com.chepics.chepics.usecase.TopicTopUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
    val comments: MutableState<List<Comment>> = mutableStateOf(emptyList())
    val selectedSet: MutableState<PickSet?> = mutableStateOf(null)
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val status: MutableState<TopicTopStatus> = mutableStateOf(TopicTopStatus.LOADING)
    val showBottomSheet: MutableState<Boolean> = mutableStateOf(false)
    val showAlert: MutableState<Boolean> = mutableStateOf(false)
    val currentSet: MutableState<PickSet?> = mutableStateOf(null)
    val showLikeCommentFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    val showLikeReplyFailureDialog: MutableState<Boolean> = mutableStateOf(false)

    fun onStart(topic: Topic) {
        this.topic.value = topic
        viewModelScope.launch {
            when (val result = topicTopUseCase.fetchPickedSet(topic.id)) {
                is CallResult.Success -> {
                    status.value = TopicTopStatus.DETAIL
                    selectedSet.value = result.data
                    currentSet.value = result.data
                    fetchTopic()
                    fetchComments(setId = result.data.id)
                }

                is CallResult.Error -> {
                    if (result.exception.message == EMPTY_RESPONSE) {
                        status.value = TopicTopStatus.TOP
                        fetchTopic()
                        return@launch
                    }

                    status.value = TopicTopStatus.FAILURE
                }
            }
        }
    }

    fun onTapLikeButton(comment: Comment) {
        viewModelScope.launch {
            when (val result =
                topicTopUseCase.like(setId = comment.setId, commentId = comment.id)) {
                is CallResult.Success -> {
                    comments.value.first { it.id == result.data.commentId }.votes =
                        result.data.count
                    comments.value.first { it.id == result.data.commentId }.isLiked =
                        result.data.isLiked
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
        topic.value?.let {
            when (val result = topicTopUseCase.fetchTopic(it.id)) {
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
                        setListUIState.value = UIState.SUCCESS
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
                            status.value = TopicTopStatus.DETAIL
                            selectedSet.value = result.data
                            currentSet.value = result.data
                            fetchTopic()
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

    private suspend fun fetchComments(setId: String) {
        when (val result = topicTopUseCase.fetchSetComments(setId = setId, offset = null)) {
            is CallResult.Success -> {
                comments.value = result.data
                commentUIState.value = UIState.SUCCESS
            }

            is CallResult.Error -> commentUIState.value = UIState.FAILURE
        }
    }
}

enum class TopicTopStatus {
    TOP,
    DETAIL,
    LOADING,
    FAILURE
}