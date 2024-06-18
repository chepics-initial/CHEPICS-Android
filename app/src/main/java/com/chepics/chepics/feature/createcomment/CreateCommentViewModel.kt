package com.chepics.chepics.feature.createcomment

import android.net.Uri
import android.webkit.URLUtil
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chepics.chepics.domainmodel.APIErrorCode
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.InfraException
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.usecase.CreateCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCommentViewModel @Inject constructor(private val createCommentUseCase: CreateCommentUseCase) :
    ViewModel() {
    val comment: MutableState<String> = mutableStateOf("")
    val link: MutableState<String> = mutableStateOf("")
    val imageUris: MutableState<List<Uri>> = mutableStateOf(emptyList())
    val replyFor: MutableState<Comment?> = mutableStateOf(null)
    val type: MutableState<CreateCommentType?> = mutableStateOf(null)
    val isLoading: MutableState<Boolean> = mutableStateOf(false)
    val showCommentRestrictionDialog: MutableState<Boolean> = mutableStateOf(false)
    val showReplyRestrictionDialog: MutableState<Boolean> = mutableStateOf(false)
    val showNetworkErrorDialog: MutableState<Boolean> = mutableStateOf(false)
    var topicId: String? = null
    var setId: String? = null
    private var parentId: String? = null

    fun onStart(
        topicId: String,
        setId: String,
        parentId: String?,
        type: CreateCommentType,
        replyFor: Comment?
    ) {
        this.topicId = topicId
        this.setId = setId
        this.parentId = parentId
        this.type.value = type
        this.replyFor.value = replyFor
    }

    fun isActive(): Boolean {
        return comment.value.trim()
            .isNotEmpty() && (link.value.isEmpty() || URLUtil.isValidUrl(link.value))
    }

    fun onTapRemoveIcon(index: Int) {
        imageUris.value = imageUris.value.filterIndexed { thisIndex, _ ->
            thisIndex != index
        }
    }

    fun onTapSubmitButton(completion: () -> Unit) {
        isLoading.value = true
        topicId?.let { topicId ->
            setId?.let { setId ->
                viewModelScope.launch {
                    var replyForProperty: List<String>? = null
                    replyFor.value?.let { comment ->
                        replyForProperty = listOf(comment.user.id)
                    }
                    when (val result = createCommentUseCase.createComment(
                        parentId = parentId,
                        topicId = topicId,
                        setId = setId,
                        comment = comment.value,
                        link = if (link.value.isNotEmpty() && URLUtil.isValidUrl(link.value)) link.value else null,
                        replyFor = replyForProperty,
                        images = imageUris.value.ifEmpty { null }
                    )) {
                        is CallResult.Success -> {
                            isLoading.value = true
                            completion()
                        }

                        is CallResult.Error -> {
                            isLoading.value = true
                            if (result.exception is InfraException.Server) {
                                if (result.exception.errorCode == APIErrorCode.ERROR_SET_NOT_PICKED) {
                                    showCommentRestrictionDialog.value = true
                                    return@launch
                                }
                                if (result.exception.errorCode == APIErrorCode.ERROR_TOPIC_NOT_PICKED) {
                                    showReplyRestrictionDialog.value = true
                                    return@launch
                                }
                            }
                            showNetworkErrorDialog.value = true
                        }
                    }
                }
            }
        }
    }
}

enum class CreateCommentType {
    COMMENT,
    REPLY;

    fun screenTitle(): String {
        return when (this) {
            COMMENT -> "コメントを作成"
            REPLY -> "リプライを作成"
        }
    }

    fun placeholder(): String {
        return when (this) {
            COMMENT -> "コメントを入力"
            REPLY -> "リプライを入力"
        }
    }
}