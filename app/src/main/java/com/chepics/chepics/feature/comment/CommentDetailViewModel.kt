package com.chepics.chepics.feature.comment

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.Comment
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
    val comment: MutableState<Comment?> = mutableStateOf(null)
    val uiState: MutableState<UIState> = mutableStateOf(UIState.LOADING)
    val replies: MutableState<List<Comment>> = mutableStateOf(emptyList())
    fun onTapImage(index: Int, images: List<String>) {
        selectedImageIndex.value = index
        commentImages.value = images
    }

    fun onStart(comment: Comment) {
        this.comment.value = comment
        viewModelScope.launch {
            fetchComment()
            fetchReplies()
        }
    }

    private suspend fun fetchComment() {
        comment.value?.let {
            when (val result = commentDetailUseCase.fetchComment(it.id)) {
                is CallResult.Success -> comment.value = result.data
                is CallResult.Error -> return
            }
        }
    }

    private suspend fun fetchReplies() {
        comment.value?.let {
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