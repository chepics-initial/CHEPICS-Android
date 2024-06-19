package com.chepics.chepics.feature.topic.comment

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.APIErrorCode
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.InfraException
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.usecase.SetCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetCommentViewModel @Inject constructor(private val setCommentUseCase: SetCommentUseCase) :
    ViewModel() {
    val set: MutableState<PickSet?> = mutableStateOf(null)
    val uiState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val comments: MutableState<List<Comment>> = mutableStateOf(emptyList())
    val showLikeCommentFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    val showLikeReplyFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    fun onStart(set: PickSet) {
        this.set.value = set
        viewModelScope.launch {
            fetchSet()
            fetchComments()
        }
    }

    fun onTapLikeButton(comment: Comment) {
        viewModelScope.launch {
            when (val result =
                setCommentUseCase.like(setId = comment.setId, commentId = comment.id)) {
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

    private suspend fun fetchSet() {
        set.value?.let {
            when (val result = setCommentUseCase.fetchSet(it.id)) {
                is CallResult.Success -> set.value = result.data
                is CallResult.Error -> return
            }
        }
    }

    private suspend fun fetchComments() {
        set.value?.let {
            when (val result = setCommentUseCase.fetchComments(setId = it.id, offset = null)) {
                is CallResult.Success -> {
                    comments.value = result.data
                    uiState.value = UIState.SUCCESS
                }

                is CallResult.Error -> uiState.value = UIState.FAILURE
            }
        }
    }
}