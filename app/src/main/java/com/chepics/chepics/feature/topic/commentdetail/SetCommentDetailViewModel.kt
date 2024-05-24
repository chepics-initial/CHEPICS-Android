package com.chepics.chepics.feature.topic.commentdetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.feature.common.UIState
import com.chepics.chepics.usecase.SetCommentDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetCommentDetailViewModel @Inject constructor(private val setCommentDetailUseCase: SetCommentDetailUseCase) :
    ViewModel() {
    val set: MutableState<PickSet?> = mutableStateOf(null)
    val comment: MutableState<Comment?> = mutableStateOf(null)
    val uiState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val replies: MutableState<List<Comment>> = mutableStateOf(emptyList())
    fun onStart(set: PickSet, comment: Comment) {
        this.set.value = set
        this.comment.value = comment
        viewModelScope.launch {
            fetchSet()
            fetchComment()
            fetchReplies()
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
        comment.value?.let {
            when (val result = setCommentDetailUseCase.fetchComment(it.id)) {
                is CallResult.Success -> comment.value = result.data
                is CallResult.Error -> return
            }
        }
    }

    private suspend fun fetchReplies() {
        comment.value?.let {
            when (val result =
                setCommentDetailUseCase.fetchReplies(commentId = it.id, offset = null)) {
                is CallResult.Success -> {
                    replies.value = result.data
                    uiState.value = UIState.SUCCESS
                }

                is CallResult.Error -> uiState.value = UIState.FAILURE
            }
        }
    }
}