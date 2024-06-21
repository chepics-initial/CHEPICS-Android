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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentDetailViewModel @Inject constructor(private val commentDetailUseCase: CommentDetailUseCase) :
    ViewModel() {
    val selectedImageIndex: MutableState<Int?> = mutableStateOf(null)
    val commentImages: MutableState<List<String>?> = mutableStateOf(null)
    val rootComment: MutableState<Comment?> = mutableStateOf(null)
    val uiState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val replies: MutableState<ImmutableList<Comment>?> = mutableStateOf(null)
    val showLikeCommentFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    val showLikeReplyFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    val showReplyRestrictionDialog: MutableState<Boolean> = mutableStateOf(false)
    val showCreateCommentScreen: MutableState<Boolean> = mutableStateOf(false)
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
                            rootComment.value = rootComment.value!!.copy(
                                votes = result.data.count,
                                isLiked = result.data.isLiked
                            )
                            return@launch
                        }
                    }
                    replies.value = replies.value?.map {
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

    fun onTapReplyButton(replyFor: Comment?) {
        rootComment.value?.let {
            viewModelScope.launch {
                when (val result = commentDetailUseCase.isSetPicked(it.topicId)) {
                    is CallResult.Success -> {
                        if (result.data) {
                            showCreateCommentScreen.value = true
                            return@launch
                        }
                        showReplyRestrictionDialog.value = true
                    }

                    is CallResult.Error -> return@launch
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
                    replies.value = result.data.toImmutableList()
                    uiState.value = UIState.SUCCESS
                }

                is CallResult.Error -> uiState.value = UIState.FAILURE
            }
        }
    }
}