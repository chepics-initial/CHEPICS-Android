package com.chepics.chepics.feature.createcomment

import android.net.Uri
import android.webkit.URLUtil
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.usecase.CreateCommentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateCommentViewModel @Inject constructor(private val createCommentUseCase: CreateCommentUseCase) :
    ViewModel() {
    val comment: MutableState<String> = mutableStateOf("")
    val link: MutableState<String> = mutableStateOf("")
    val imageUris: MutableState<List<Uri>> = mutableStateOf(emptyList())
    val replyFor: MutableState<Comment?> = mutableStateOf(null)
    val type: MutableState<CreateCommentType?> = mutableStateOf(null)
    var topicId: String? = null
    var setId: String? = null

    fun onStart(topicId: String, setId: String, type: CreateCommentType, replyFor: Comment?) {
        this.topicId = topicId
        this.setId = setId
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