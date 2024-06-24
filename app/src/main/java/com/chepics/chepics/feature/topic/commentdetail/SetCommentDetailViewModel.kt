package com.chepics.chepics.feature.topic.commentdetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.APIErrorCode
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.InfraException
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.feature.common.FooterStatus
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.usecase.SetCommentDetailUseCase
import com.chepics.chepics.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetCommentDetailViewModel @Inject constructor(private val setCommentDetailUseCase: SetCommentDetailUseCase) :
    ViewModel() {
    val set: MutableState<PickSet?> = mutableStateOf(null)
    val rootComment: MutableState<Comment?> = mutableStateOf(null)
    val uiState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val replies: MutableState<ImmutableList<Comment>?> = mutableStateOf(null)
    val showLikeCommentFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    val showLikeReplyFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    val footerStatus: MutableState<FooterStatus> = mutableStateOf(FooterStatus.LOADINGSTOPPED)
    fun onStart(set: PickSet, comment: Comment) {
        this.set.value = set
        this.rootComment.value = comment
        viewModelScope.launch {
            fetchSet()
            fetchComment()
            fetchReplies()
        }
    }

    fun onTapLikeButton(comment: Comment) {
        viewModelScope.launch {
            when (val result =
                setCommentDetailUseCase.like(setId = comment.setId, commentId = comment.id)) {
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

    fun onReachFooterView() {
        rootComment.value?.let {
            if (footerStatus.value == FooterStatus.LOADINGSTOPPED || footerStatus.value == FooterStatus.FAILURE) {
                footerStatus.value = FooterStatus.LOADINGSTARTED
                viewModelScope.launch {
                    when (val result = setCommentDetailUseCase.fetchReplies(
                        commentId = it.id,
                        offset = replies.value?.size
                    )) {
                        is CallResult.Success -> {
                            val updatedReplies = replies.value?.toMutableList()
                            for (additionalReply in result.data) {
                                val index =
                                    replies.value?.indexOfFirst { it.id == additionalReply.id }
                                if (index != null && index != -1) {
                                    updatedReplies?.set(index, additionalReply)
                                } else {
                                    updatedReplies?.add(additionalReply)
                                }
                            }
                            replies.value = updatedReplies?.toImmutableList()
                            if (result.data.size < Constants.ARRAY_LIMIT) {
                                footerStatus.value = FooterStatus.ALLFETCHED
                            } else {
                                footerStatus.value = FooterStatus.LOADINGSTOPPED
                            }
                        }

                        is CallResult.Error -> footerStatus.value = FooterStatus.FAILURE
                    }
                }
            }
        }
    }

    private suspend fun fetchSet() {
        set.value?.let {
            when (val result = setCommentDetailUseCase.fetchSet(it.id)) {
                is CallResult.Success -> set.value = result.data
                is CallResult.Error -> return
            }
        }
    }

    private suspend fun fetchComment() {
        rootComment.value?.let {
            when (val result = setCommentDetailUseCase.fetchComment(it.id)) {
                is CallResult.Success -> rootComment.value = result.data
                is CallResult.Error -> return
            }
        }
    }

    private suspend fun fetchReplies() {
        rootComment.value?.let {
            when (val result =
                setCommentDetailUseCase.fetchReplies(commentId = it.id, offset = null)) {
                is CallResult.Success -> {
                    replies.value = result.data.toImmutableList()
                    if (result.data.size < Constants.ARRAY_LIMIT) {
                        footerStatus.value = FooterStatus.ALLFETCHED
                    } else {
                        footerStatus.value = FooterStatus.LOADINGSTOPPED
                    }
                    uiState.value = UIState.SUCCESS
                }

                is CallResult.Error -> uiState.value = UIState.FAILURE
            }
        }
    }
}