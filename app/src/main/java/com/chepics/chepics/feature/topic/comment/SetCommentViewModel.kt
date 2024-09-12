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
import com.chepics.chepics.feature.common.FooterStatus
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.usecase.SetCommentUseCase
import com.chepics.chepics.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetCommentViewModel @Inject constructor(private val setCommentUseCase: SetCommentUseCase) :
    ViewModel() {
    val set: MutableState<PickSet?> = mutableStateOf(null)
    val uiState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val comments: MutableState<ImmutableList<Comment>?> = mutableStateOf(null)
    val showLikeCommentFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    val showLikeReplyFailureDialog: MutableState<Boolean> = mutableStateOf(false)
    val footerStatus: MutableState<FooterStatus> = mutableStateOf(FooterStatus.LOADINGSTOPPED)
    private var offset = 0

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

    fun onReachFooterView() {
        set.value?.let {
            if (footerStatus.value == FooterStatus.LOADINGSTOPPED || footerStatus.value == FooterStatus.FAILURE) {
                footerStatus.value = FooterStatus.LOADINGSTARTED
                viewModelScope.launch {
                    when (val result = setCommentUseCase.fetchComments(
                        setId = it.id,
                        offset = offset
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
                                footerStatus.value = FooterStatus.ALLFETCHED
                                offset = 0
                                return@launch
                            }
                            footerStatus.value = FooterStatus.LOADINGSTOPPED
                            offset += Constants.ARRAY_LIMIT
                        }

                        is CallResult.Error -> footerStatus.value = FooterStatus.FAILURE
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
                    comments.value = result.data.toImmutableList()
                    if (result.data.size < Constants.ARRAY_LIMIT) {
                        footerStatus.value = FooterStatus.ALLFETCHED
                    } else {
                        footerStatus.value = FooterStatus.LOADINGSTOPPED
                    }
                    uiState.value = UIState.SUCCESS
                    offset = Constants.ARRAY_LIMIT
                }

                is CallResult.Error -> uiState.value = UIState.FAILURE
            }
        }
    }
}