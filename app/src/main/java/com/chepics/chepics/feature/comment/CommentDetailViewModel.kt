package com.chepics.chepics.feature.comment

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.APIErrorCode
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.InfraException
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.usecase.CommentDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentDetailViewModel @Inject constructor(private val commentDetailUseCase: CommentDetailUseCase) :
    ViewModel() {
    val selectedImageIndex: MutableState<Int?> = mutableStateOf(null)
    val commentImages: MutableState<List<String>?> = mutableStateOf(null)
    val rootComment: MutableState<Comment?> = mutableStateOf(null)
    val uiState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val replies: MutableState<List<Comment>> = mutableStateOf(emptyList())
    val showLikeCommentFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    val showLikeReplyFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    fun onTapImage(index: Int, images: List<String>) {
        selectedImageIndex.value = index
        commentImages.value = images
    }

    fun onStart(comment: Comment) {
        this.rootComment.value = comment
        viewModelScope.launch {
            fetchComment()
            fetchReplies()
        }
    }

    fun onTapLikeButton(comment: Comment) {
        viewModelScope.launch {
            when (val result =
                commentDetailUseCase.like(setId = comment.setId, commentId = comment.id)) {
                is CallResult.Success -> {
                    rootComment.value?.let {
                        if (it.id == result.data.commentId) {
                            rootComment.value?.votes = result.data.count
                            rootComment.value?.isLiked = result.data.isLiked
                        }
                    }
                    replies.value.first { it.id == result.data.commentId }.votes = result.data.count
                    replies.value.first { it.id == result.data.commentId }.isLiked =
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

    private suspend fun fetchComment() {
        rootComment.value?.let {
            when (val result = commentDetailUseCase.fetchComment(it.id)) {
                is CallResult.Success -> rootComment.value = result.data
                is CallResult.Error -> return
            }
        }
    }

    private suspend fun fetchReplies() {
        rootComment.value?.let {
            when (val result =
                commentDetailUseCase.fetchReplies(commentId = it.id, offset = null)) {
                is CallResult.Success -> {
                    replies.value = result.data
                    uiState.value = UIState.SUCCESS
                }

                is CallResult.Error -> uiState.value = UIState.FAILURE
            }
        }
    }
}